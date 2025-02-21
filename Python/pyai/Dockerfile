# Docker 이미지 설정 파일
# 베이스 이미지 설정_파이썬 설정
FROM python:3.12.7

# 작업 디렉터리 설정 : requirements.txt 와 app파일 존재위치
WORKDIR /app

# 의존성 파일을 WORKDIR 디렉토리로 복사
# 처음에는 오직 요구 조건이 필요한 파일만 복사, 이외의 코드는 그대로 둘 것
COPY requirements.txt /app/

# 의존성 파일의 패키지 설치 : 해당 파이썬에 필요한 모든 패키지 넣을 것
# 없는 것 : pip, 업데이터 : upgrade
RUN pip install --no-cache-dir --upgrade -r requirements.txt \
    || { echo "Installation failed"; exit 1; }

# 패키지 업데이트 및 opencv 용 추가  : libgil-mesa-glx 패키지
#  && rm -rf /var/lib/apt/lists/*  : 리눅스 환경의 APT 패키지 삭제
RUN apt-get update && apt-get -y install libgl1-mesa-glx libglib2.0-0 \
    libsm6 libxext6 libxrender-dev

# 애플리케이션 코드 복사
# COPY ./app /code/app
COPY . .

# 서버 실행
# main 서버위치 위치 및 포트번호
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8001"]



