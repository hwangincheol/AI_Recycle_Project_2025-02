# 데이터파 위치 확인
# import os
# path = "Recycle-1/data.yaml"
# if os.path.exists(path):
#     print(os.path.dirname(path))
# else:
#     print("없음")

# gpu확인 여부
import torch
# check_gpu()
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

from function import *

############################################################
if __name__ == '__main__':
    # https://velog.io/@uvictoli/TIL-RuntimeError-freezesupport
    from multiprocessing.spawn import freeze_support
    freeze_support()

    #선택 1_훈련, 2_모델확인
    select = 1

    # 훈련
    yolo_name = 'yolov9t'
    epochs = 500
    train_name = 'yo9_02'
    save_name = train_name

    # 평가
    model_name = "yo9_01"

    ###############################################################
    if select == 1:
        #훈련
        print('저장 파일 이름 : ',save_name)
        if device == torch.device('cuda'):
            model_train(yolo_name, epochs, train_name, save_name)
            # print('gpu상태')
        else:
            print('cpu 상태')
    ###############################################################
    elif select == 2:
        # 훈련평가
        print("훈련평가")
        model_result_load(model_name)

    ###############################################################




