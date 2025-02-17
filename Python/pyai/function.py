# main용 function
import io
import base64

import cv2
import numpy as np

from pydantic import BaseModel
from PIL import Image, ImageDraw
from ultralytics import YOLO

from model.function import load_model_api

import logging
logging.basicConfig(level=logging.INFO)

# 메시지출력 및 이미지 출력함수
class DetectionResult(BaseModel):
    # image : str = None
    json_data : dict[str, list]

############################ roboflow #############################
#roboflow사이트 용
# 모델 예측
def model_predict_robo(image, confidence, overlap):
    model = load_model_api()
    img = np.array(image)  # 이미지 numpy 배열 반환
    result_image = model.predict(img, confidence=confidence, overlap=overlap).json()
    return result_image

#바운딩박스
def draw_bounding_box_robo(image: Image,result):
    draw=ImageDraw.Draw(image)

    for pred in result['predictions']:
        # 예측된 박스의 xy 중점
        x, y, width, height= pred['x'], pred['y'], pred['width'], pred['height']
        class_name, confidence = pred['class'], pred['confidence']

        x1 = int(x - width/2)
        y1 = int(y - height/2)
        x2 = int(x + width/2)
        y2 = int(y + height/2)

        #바운딩박스
        draw.rectangle([(x1, y1), (x2, y2)], outline="red", width=3)
        # 글자박스
        draw.rectangle([(x1, y1-20), (x1+width, y1)], fill="white")
        label = f'{class_name} {confidence : .2f}'
        draw.text((x1, y1-15), label, fill="black", size=30)
    return image

# 결과 JSON 파일
def print_json_robo(result_json):
    data = {'predictions': []}

    for pred in result_json['predictions']:
        x, y, width, height = pred['x'], pred['y'], pred['width'], pred['height']
        class_name, confidence, class_id = pred['class'], pred['confidence'], pred['class_id']
        data['predictions'].append({
            "x" : x,
            "y" : y,
            "width" : width,
            "height" : height,
            "class_id" : class_id,
            "class_name" : class_name,
            "confidence" : confidence
        })
    return data

############################ pt #############################
# .pt 파일용
# 모델 예측
def model_predict_pt(model_name,image):
    # 모델 불러오기
    # https://coffeedjimmy.github.io/pytorch/2019/11/05/pytorch_nograd_vs_train_eval/
    model = YOLO(model_name)
    img = np.array(image)
    result_image = model(img)
    class_names = model.names
    return result_image, class_names

#바운딩 박스
def draw_bounding_box_pt(img: Image, results, class_names):

    for result in results:
        boxes = result.boxes.xyxy  # 바운딩 박스
        confidences = result.boxes.conf  # 신뢰도
        class_ids = result.boxes.cls  # 클래스 이름

        for box, confidences, class_ids in zip(boxes, confidences, class_ids):

            x1, y1, x2, y2 = map(int, box)  # 좌표를 정수로 변환
            label = class_names[int(class_ids)]  # 클래스 이름
            # 이미지, 왼쪽상단위치, 오른쪽하단위치, 선색깔, 선두께
            cv2.rectangle(img, (x1, y1), (x2, y2), (255, 0, 0), 2)
            # 이미지, 텍스트, 좌표, 폰트, 폰트크기, 폰트색, 폰트두께
            cv2.putText(img, f'{label} {confidences : .2f}', (x1, y1), cv2.FONT_HERSHEY_SIMPLEX, 0.9, (255, 0, 0), 2)

    image = Image.fromarray(img)  # 결과 이미지를 PIL로 변환
    return image

# json 결과
def print_json_pt(results, class_names):
    data = []
    for result in results:
        class_ids = result.boxes.cls  # 클래스 이름
        # 타입확인
        # logging.info(type(class_ids))
        # logging.info(class_ids)
        # class_name cpu에서의 변환
        class_name = class_ids.cpu().numpy()
        # logging.info(type(class_name))
        # logging.info(int(class_name[0]))
        for i in range(len(class_name)):
            class_id = int(class_ids[i])
            data.append({
                "class_name" : class_names[class_id],
            })
    return data

#예측결과 중복제거
def remove_result_duplicate(frame_result_prev):
    frame_result = {"result": []}
    list_tmp = []
    for result_list in frame_result_prev['prev']:
        # print(list)
        for class_name in result_list:
            # print(type(class_name["class_name"]))
            name = class_name["class_name"]
            list_tmp.append(name)

    # 중복제거로 set방식적용
    set_list = set(list_tmp)
    list_tmp = list(set_list)
    frame_result["result"] = list_tmp
    # print("최종 : ", frame_result)
    return frame_result

############################이미지#############################
# 알파채널(A_필터)이 있다면 제거하고 RGB로 변환
def removal_filter(image):
    if image.mode != 'RGB':
        image = image.convert('RGB')
    return image

# 이미지 결과를 base64로 인코딩
def encode_base64(image):
    buffered = io.BytesIO()
    image.save(buffered, format='JPEG')  # JPEG로 이미지저장
    img_str = base64.b64encode(buffered.getvalue()).decode("utf-8")
    return img_str

########################### web cap용 ###############################
# web캡 전용
#연결용 함수
def on_connect(client, userdata, flags, rc):
    print(f"connected with result code {rc}")

# 객체 감지용 색상 함수
def get_colors(num_colors):
    np.random.seed(0)
    colors = [tuple(np.random.randint(0, 255, 3).tolist()) for _ in range(num_colors)]
    return colors

#모델 객체를 탐지용 함수
def detect_objects(model, colors, image: np.array):
    results = model(image, verbose=False)
    class_names = model.names

    for result in results:
        boxes = result.boxes.xyxy
        confidences = result.boxes.conf
        class_ids = result.boxes.cls
        for box, confidence, class_id in zip(boxes, confidences, class_ids):
            x1, y1, x2, y2 = map(int, box)
            label = class_names[int(class_id)]
            cv2.rectangle(image, (x1, y1), (x2,y2), colors[int(class_id)], 2)
            cv2.putText(image, f'{label} {confidence:.2f}', (x1,y1),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.9, colors[int(class_id)], 2)

    return image