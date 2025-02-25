# ✨ Recycle_Project
RecyclePic  - 사진을 찍어 수거를 돕는 웹 프로젝트
<br/>
MBC 아카데미 Spring Boot & Python & AI 연계 팀 프로젝트 with 김정현, 윤인영, 이혜린, 황인철
<br/>
<br/>
🗣 PPT(pdf) : [AI프로젝트_1석3조.pdf](https://github.com/user-attachments/files/18953597/AI._1.3.pdf)
<br/>
<br/>
## 👀 프로젝트 소개
📝 목표

─────────────────────────────────────────────────────────────────────────

이미지, 영상을 활용한 가전제품 폐기물 수거 관리 웹 프로젝트
<br/>
<br/>
![1](https://github.com/user-attachments/assets/bb25991d-807d-4f0a-9d8b-5cae3e9227ee)
<br/>
📝 역할

─────────────────────────────────────────────────────────────────────────

⚜ 김정현(조장)
<br/>
더미데이터 라벨링, 훈련, 파이썬 & 스프링부트 연계 작업
<br/>
<br/>
🌟 윤인영
<br/>
공지사항 카테고리 CRUD 작업
<br/>
<br/>
🌟 이혜린
<br/>
멤버 카테고리, 전반적인 css 작업, ppt 초안 작업
<br/>
<br/>
🌟 황인철
<br/>
수거 신청 기능 구현, 신청 내역 관리
<br/>
<br/>
<br/>
📝 개발기간

─────────────────────────────────────────────────────────────────────────

2025.02.03 ~ 2025.02.21
<br/>
<br/>
![2](https://github.com/user-attachments/assets/e8186242-f1ce-4471-a12c-ab18e5950c5e)
<br/>
<br/>
<br/>
## 👀 기술 스텍
✔️ HTML, CSS, JavaScript
<br/>
✔️ Ajax, JQuery
<br/>
✔️ Oracle, Docker
<br/>
✔️ Java, SpringBoot
<br/>
✔️ Git
<br/>
✔️ Python
<br/>
✔️ Roboflow
<br/>
<br/>
![3](https://github.com/user-attachments/assets/785b0c0d-f443-4737-bb8d-13aad2f2c2ed)
<br/>
<br/>
<br/>
<br/>
## 👀 더미 데이터
📝 서치

─────────────────────────────────────────────────────────────────────────

AIHub(https://www.aihub.or.kr/aihubdata/data/view.do?currMenu=115&topMenu=100&aihubDataSe=data&dataSetSn=140) 사이트를 이용하여 더미데이터 서치.
<br/>
<br/>
![4](https://github.com/user-attachments/assets/a62a16f1-201e-4304-83bc-d4e093e5fa19)
<br/>
<br/>
<br/>
📝 라벨링

─────────────────────────────────────────────────────────────────────────

AIHub에서 제공하는 Json으로 파일 변환.
<br/>
<br/>
![AI프로젝트_1석3조](https://github.com/user-attachments/assets/25df7040-f917-47a6-9d8c-b62d54eb5e6c)
![AI프로젝트_1석3조 (1)](https://github.com/user-attachments/assets/23976669-3a6b-4700-b841-c42e3e934220)
<br/>
<br/>
<br/>
📝 데이터 훈련

─────────────────────────────────────────────────────────────────────────

라벨링한 데이터를 훈련.
<br/>
<br/>
![6](https://github.com/user-attachments/assets/6bec7260-bf04-4b8d-92ed-47259e627e74)
![7](https://github.com/user-attachments/assets/ffef4c5b-e753-458f-b866-fe704b23a895)
<br/>
<br/>
<br/>
## 👀 페이지 구성
📝 초기 작업

─────────────────────────────────────────────────────────────────────────

‍🗨 Security CSRF 토큰
<br/>
위조 요청 공격에 대한 보안을 강화.
<br/>
<br/>
‍🗨 계정 생성
<br/>
관리자, 일반 유저에 대한 계정 미리 생성
<br/>
<br/>
‍🗨 Thymeleaf layout
<br/>
타임리프에서 제공하는 레이아웃 기능 활성화.
<br/>
<br/>
‍🗨 팝업 커스텀
<br/>
alert, confirm, loading 팝업창 커스텀.
<br/>
<br/>
‍🗨 에러 페이지
<br/>
403, 404, 405, 500 에러 페이지 커스텀.
<br/>
<br/>
![9](https://github.com/user-attachments/assets/cd2b4b82-298c-4d40-9c47-c6d64025ba9b)
![10](https://github.com/user-attachments/assets/e6e30195-d569-42c3-a3c7-9df92b972c2d)
<br/>
<br/>
<br/>
📝 Member 카테고리

─────────────────────────────────────────────────────────────────────────

‍🗨 회원가입
개인 정보 입력으로 회원가입 가능.(비밀번호 암호화, 다음 API, 아이디 중복확인, 관리자는 'admin'으로 회원가입을 해야 진입 가능)
<br/>
<br/>
‍🗨 로그인
<br/>
Security를 활용한 로그인, 이용 가능 사이트 제한.
<br/>
<br/>
‍🗨 아이디/비밀번호 찾기
<br/>
간단한 회원 정보 입력으로 아이디 및 비밀번호 찾기 기능 부여.
<br/>
<br/>
‍🗨 마이페이지
<br/>
비밀번호 확인을 통한 마이페이지.(아이디를 제외한 개인정보 수정 가능)
<br/>
<br/>
![12](https://github.com/user-attachments/assets/4d9812ec-9f65-4bed-8f7e-f51dcb7ef424)
![13](https://github.com/user-attachments/assets/8d1853ac-ad95-4b10-8b43-5b2af513cbe8)
<br/>
<br/>
<br/>
📝 Board 카테고리

─────────────────────────────────────────────────────────────────────────

‍🗨 등록
<br/>
관리자만 공지사항 등록 가능.(이미지 및 그 외 파일 업로드 가능)
<br/>
<br/>
‍🗨 리스트
<br/>
관리자가 등록한 공지사항 리스트업.(페이징 처리로 10개씩 리스트업)
<br/>
<br/>
‍🗨 상세
<br/>
관리자가 등록한 공지사항을 볼 수 있음.(이미지 및 그 외 파일 다운로드 가능)
<br/>
<br/>
‍🗨 수정
<br/>
관리자가 공지사항을 수정.
<br/>
<br/>
‍🗨 삭제
<br/>
관리자가 공지사항을 삭제.
<br/>
<br/>
![15](https://github.com/user-attachments/assets/0aa608e4-0ddb-4cc3-a5ea-9bc2693b69e4)
![16](https://github.com/user-attachments/assets/2b1dce04-7b23-4393-8170-3d0fbd4cc6d4)
<br/>
<br/>
<br/>
📝 Collect 카테고리

─────────────────────────────────────────────────────────────────────────

‍🗨 수거 신청
<br/>
유저가 버릴 폐기물을 수거 신청하는 페이지.(사진 및 동영상을 업로드하면 AI가 품목 자동 인식으로 분류 가능)
<br/>
<br/>
‍🗨 유저 수거 신청 내역
<br/>
유저가 본인의 수거 신청 내역을 볼 수 있는 페이지. 진행 상황을 파악할 수 있음.
<br/>
<br/>
‍🗨 관리자 수거 신청 내역
<br/>
각 유저들의 수거 신청 내역을 보고 취소, 수거, 완료 진행 상태를 수정할 수 있음.
<br/>
<br/>
![슬라이드48](https://github.com/user-attachments/assets/4acc01b4-49f3-40b5-950e-890b8039539f)
![슬라이드49](https://github.com/user-attachments/assets/b464d7d2-9cce-4826-ab2a-3b6ea3e23675)
![슬라이드50](https://github.com/user-attachments/assets/ef9cd971-7be1-46ac-a26c-b405cea92a4a)
![슬라이드51](https://github.com/user-attachments/assets/eafb8248-ed0c-4ac4-9da2-e888bacfff36)
![슬라이드52](https://github.com/user-attachments/assets/f381e43a-ba25-4a9d-9162-97a67da3cad0)
![슬라이드53](https://github.com/user-attachments/assets/0ef02558-4925-470f-b08c-25446ea2fad2)
<br/>
<br/>
<br/>
## 👀 JAVA + PYTHON
📝 연동

─────────────────────────────────────────────────────────────────────────

Java와 Python 사이를 Json을 이용하여 통신하도록 작업.
<br/>
<br/>
![21](https://github.com/user-attachments/assets/7de48264-7734-4c34-aecb-42143a7817db)
![24](https://github.com/user-attachments/assets/17e6be85-cd1e-492a-963f-7a37b3ccdcf7)
![22](https://github.com/user-attachments/assets/9b671db7-a149-458b-8e14-d0bc33f20c5a)
<br/>
<br/>
<br/>
📝 배포

─────────────────────────────────────────────────────────────────────────

Python 서버를 도커를 사용하여 배포하도록 작업.
<br/>
<br/>
![23](https://github.com/user-attachments/assets/87d151f4-27c3-43cc-958f-1bec2c47f075)
