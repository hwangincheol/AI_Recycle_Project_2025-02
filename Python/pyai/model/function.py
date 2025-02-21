############################################################################
# gpu, torch 사용 버전 확인
def check_gpu():
    import torch
    import torchvision
    import torchaudio
    print("GPU avilable:",torch.cuda.is_available()) #gpu 사용확인
    print("PyTorch version:", torch.__version__)
    print("torchaudio version:", torchaudio.__version__)
    print("torchvision version:", torchvision.__version__)

############################################################################
#모델불러오기  roboflow
def load_model_api(yolo_version = None):
    from dotenv import load_dotenv
    import os
    from roboflow import Roboflow
    #.env 보안코드 호출
    load_dotenv()
    api_key = os.getenv("ROBOFLOW_API_KEY")
    workspace = os.getenv("workspace")
    project = os.getenv("project")
    version = os.getenv("version")

    #roboflow_model 호출
    rf = Roboflow(api_key=api_key)                      #roboflow 접속
    project = rf.workspace(workspace).project(project)  #훈련 workspase선택
    version = project.version(version)                  #훈련모델버전선택
    # dataset = version.download(yolo_version)          #데이터베이스다운
    model = version.model
    return model

def load_model_url():
    from dotenv import load_dotenv
    import os

    load_dotenv()
    url = os.getenv("Model_URL")
    return url

#########################################################################
# 모델 훈련(https://sarrstudy.tistory.com/262)
def model_train(yolo_name, epochs, train_name, save_name):
    from ultralytics import YOLO
    # 기준모델
    yolo_name = "yolo/" + yolo_name + '.pt'
    model = YOLO(yolo_name)

    # 데이터 정보_절대경로로 이용
    # 왜 C:\python-workspace\BootPyAI\pyai\datasets\Recycle-1\valid\images 로 되는지 의문?
    dataset = 'C:/Users/wjdugs842/Desktop/teamproject/pyai/model/Recycle-1/data.yaml'

    # 훈련진행
    model.train(
        name=train_name,
        data=dataset,  # 데이터셋 경로
        epochs=epochs,  # 에포크 수
        batch=32,  # 모델에 한번에 들어가는 데이터집합크기 64/32/16
        momentum=0.9,  # 모멘텀(0.9~0.99_높음, 0.6~0.85_낮음)
        lr0=0.001,  # 초기 학습률
        lrf=0.01,  # 학습률 감소비율_학습률 최종값
        weight_decay=0.0005,  # 가중치 감소
        imgsz=320,  # 입력 이미지 크기 640/320
        optimize=False,  # 모델훈련시 최적화중지
        augment=True,  # 데이터 증강 사용_과적합방지
        optimizer='SGD',  # 최적화 알고리즘
        patience=10,  # 인내심_조기종료
        device=0,  # 사용할 GPU 장치
        val = True #검증여부
    )

    # 훈련 모델 저장
    save_name = 'pt/' + save_name + '.pt'
    model.save(save_name)

# 모델결과 Excel 저장
def model_excel_save(model_name, epoch, train_precision, train_map50, train_map95, train_recall):
    # 엑셀프로그램 실행
    import win32com.client
    excel = win32com.client.Dispatch("Excel.Application")
    excel.Visible = False  # 실행된 엑셀 시각화여부

    # 기존파일 열기
    print("엑셀파일 열기")
    wb = excel.Workbooks.Open('C:/Users/wjdugs842/Desktop/훈련결과.xlsx')

    # 위크시트 추가 및 설정
    print("워크시트 추가")
    ws = wb.Worksheets.Add()
    ws.Name = model_name  # 시트이름

    # 값 추가
    print("값추가")

    # 명칭추가
    # modelname	epoch	precision	train_map50, train_map95, train_recall
    ws.cells(1, 1).value = 'model_name'  # A1
    ws.cells(1, 2).value = 'epoch'  # B1
    ws.cells(1, 3).value = 'precision'
    ws.cells(1, 4).value = 'mAP50'
    ws.cells(1, 5).value = 'mAP50-95'
    ws.cells(1, 6).value = 'recall'
    ws.cells(1,7).value = 'train_box_loss'

    # 모델명 및 epoch 추가
    ws.cells(2, 1).value = model_name
    ws.cells(2, 2).value = epoch

    # epoch에 대한 셀크기 계산
    cell_num = str(2 + epoch - 1)
    pre_cell = 'C2:C' + cell_num
    map1_cell = 'D2:D' + cell_num
    map2_cell = 'E2:E' + cell_num
    test_cell = 'F2:F' + cell_num

    # 리스트형식 값추가
    ws.Range(pre_cell).Value = [[p] for p in train_precision]
    ws.Range(map1_cell).Value = [[t] for t in train_map50]
    ws.Range(map2_cell).Value = [[v] for v in train_map95]
    ws.Range(test_cell).Value = [[e] for e in train_recall]

    wb.Save()  # 파일 저장
    wb.Close()  # 워크북 닫기
    excel.Quit()  # 엑셀 닫기
    print("파일저장 종료")

# 모델 결과 가져오기
def model_result_load(model_name):
    # 모델 저장 & 불러오기
    # https://tutorials.pytorch.kr/beginner/saving_loading_models.html
    import torch

    # 훈련결과_dict형태
    model = 'pt/' + model_name + '.pt'
    checkpoint = torch.load(model, weights_only=False)

    # 훈련정도 출력_list형태
    # train_args : 훈련 파라미터 정보
    epoch = checkpoint['train_args']['epochs']

    # train_results : 훈련결과 정도
    train_precision = checkpoint['train_results']['metrics/precision(B)']  # 정확도

    # box_loss : 바운딩박스 예측 손실
    train_box_loss = checkpoint['train_results']['train/box_loss']
    val_box_loss = checkpoint['train_results']['val/box_loss']
    # cls_loss : 객체 클래스 예측 손실
    train_cls_loss = checkpoint['train_results']['train/cls_loss']
    val_cls_loss = checkpoint['train_results']['val/cls_loss']

    # mAP train 검증
    train_map50 = checkpoint['train_results']['metrics/mAP50(B)']
    train_map95 = checkpoint['train_results']['metrics/mAP50-95(B)']

    #재헌율 metrics/recall(B)
    train_recall = checkpoint['train_results']['metrics/recall(B)']

    # 모델 excel 저장
    model_excel_save(model_name, epoch, train_precision, train_map50, train_map95, train_recall)

# 그래프그리기
def model_plot(train_precision, train_box_loss, val_box_loss):
    import os
    import matplotlib.pyplot as plt

    os.environ['KMP_DUPLICATE_LIB_OK'] = 'True'
    plt.plot(train_precision)
    plt.plot(train_box_loss)
    plt.plot(val_box_loss)
    plt.xlabel('epoch')
    plt.ylabel('loss')
    plt.legend(['precision', 'train_loss', 'val_loss'])
    plt.show()

