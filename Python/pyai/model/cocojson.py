import os
import re
import json

def process_json_files_in_tv_folder(tv_folder, stroge_path, category_id):
    # TV 폴더 내 모든 하위 폴더를 순회
    for folder_name in os.listdir(tv_folder):
        folder_path = os.path.join(tv_folder, folder_name)

        # 하위 폴더인지 확인
        if os.path.isdir(folder_path) and folder_name.startswith('20_X'):
            # 하위 폴더 내 모든 파일을 순회
            for item in os.listdir(folder_path):
                item_path = os.path.join(folder_path, item)

                # JSON 파일인지 확인
                if item.endswith('.Json'):
                    try:
                        # JSON 파일 읽기
                        with open(item_path, 'r', encoding='utf-8') as json_file:
                            data = json.load(json_file)

                        # 데이터 구조 확인
                        if isinstance(data, dict):
                            data = [data]  # 단일 객체를 리스트로 변환

                        # 데이터 변환
                        images = []
                        annotations = []
                        categories = []

                        # 이미지 정보 추출
                        for index, item in enumerate(data):
                            if not isinstance(item, dict):
                                print(f"Unexpected item format: {item}")
                                continue

                            file_name = item.get("FILE NAME")
                            if not file_name:
                                print(f"Missing 'FILE NAME' in item: {item}")
                                continue

                            # 파일 이름에서 ID 추출
                            match = re.search(r'_(\d+_\d+)(?:_\d+)?\.jpg$', file_name)
                            if match:
                                id_part = match.group(1).replace('_', '')  # '_'를 제거하여 ID 생성
                                image_id = int(id_part)  # 정수형으로 변환

                                images.append({
                                    "id": image_id,
                                    "file_name": file_name,
                                    "width": item["RESOLUTION"].split('*')[0],
                                    "height": item["RESOLUTION"].split('*')[1]
                                })

                                # 어노테이션 정보 추출
                                if item.get("BoundingCount") and int(item["BoundingCount"]) > 0:  # BoundingCount가 0보다 큰지 확인
                                    bbox = item["Bounding"][0]  # 첫 번째 Bounding 객체
                                    annotations.append({
                                        "id": index + 1,
                                        "image_id": image_id,
                                        "category_id": category_id,
                                        "bbox": [
                                            int(bbox["x1"]),
                                            int(bbox["y1"]),
                                            int(bbox["x2"]) - int(bbox["x1"]),
                                            int(bbox["y2"]) - int(bbox["y1"])
                                        ],
                                        "area": (int(bbox["x2"]) - int(bbox["x1"])) * (int(bbox["y2"]) - int(bbox["y1"])),
                                        "iscrowd": 0
                                    })

                        # 카테고리 정보
                        categories = [
                            {"id": 1, "name": "Oven_Range"},
                            {"id": 2, "name": "Air_Purifier"},
                            {"id": 3, "name": "Refrigerator"},
                            {"id": 4, "name": "Printer"},
                            {"id": 5, "name": "Washing_Machine"},
                            {"id": 6, "name": "Air_Conditioner"},
                            {"id": 7, "name": "Audio"},
                            {"id": 8, "name": "Heater"},
                            {"id": 9, "name": "Desktop_Computer"},
                            {"id": 10, "name": "Keyboard"},
                            {"id": 11, "name": "Monitor"},
                            {"id": 12, "name": "Rice_Cooker"},
                            {"id": 13, "name": "Fan"},
                            {"id": 14, "name": "Vacuum_Cleaner"},
                            {"id": 15, "name": "Water_Purifier"},
                            {"id": 16, "name": "Blender"},
                            {"id": 17, "name": "Iron"}
                        ]

                        # 새로운 JSON 구조 만들기
                        new_data = {
                            "images": images,
                            "annotations": annotations,
                            "categories": categories
                        }

                        # 변환된 데이터를 새로운 파일로 저장
                        os.makedirs(stroge_path, exist_ok=True)  # 경로가 없으면 생성
                        new_file_name = f'converted_{os.path.basename(item_path)}'  # item_path에서 파일 이름만 추출
                        new_file_path = os.path.join(stroge_path, new_file_name)
                        with open(new_file_path, 'w', encoding='utf-8') as new_json_file:
                            json.dump(new_data, new_json_file, ensure_ascii=False, indent=4)

                    except Exception as e:
                        print(f"Error processing file {item_path}: {e}")


# 중요경로
main_path = '파일 위치'
#카데고리
category_name = '컴퓨터모니터'
category_id = 11
#세부경로
tv_folder_path = main_path+'전자제품/' + category_name
stroge_path = main_path + 'json변환/' + category_name


# 변환 함수 호출
process_json_files_in_tv_folder(tv_folder_path, stroge_path, category_id)
