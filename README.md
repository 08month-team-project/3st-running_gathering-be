# Runto_BE

## Introduction

__FE와 AWS를 연동해 웹 서버를 배포한 프로젝트__

- **진행기간**: 2024.10.14 ~ 2024.11.15

- **진행 방식**
  - Notion 가이드 요구사항 및 기능 확인
  - 기능별 역할 분배
  - API 명세서 작성
  - ERD 설계
  - 프로젝트 기본 세팅
  - 기능 구현 및 테스트
  - Github PR 및 팀원 Review 후 Merge
  - AWS EC2를 통해 웹 서버 배포
  - 배포후 발생한 Bug Fix

- **프로젝트 전체 방향성**
  - FE팀과 소통할 인터페이스를 협의하고 정의하는 역량 향상
  - BE팀 내 적극적인 소통을 통해 적절한 업무 분배 역량 향상
  - AWS를 통해 서버 배포 경험

## Feature

### 🌐 회원
- #### 회원가입
  - ###### 이메일 중복 확인

- #### 로그인
  - ###### 소셜로그인(카카오)
- #### 로그아웃
 
- #### 회원탈퇴
  
- #### 마이프로필

- #### 생성 및 참여한 모임 조회

- #### 내 이벤트

- #### 런닝 달력
  
### 🌐 모임
- #### 모임 등록
   - ###### 이미지 업로드
   - ###### 사이즈 조회
   - ###### 카테고리 조회
   - ###### 위치 정보 

- #### 모임 조회 및 검색
   - ###### 카테고리
   - ###### 이름
   - ###### 상태
   - ###### 정렬
   - ###### 페이지 번호

- #### 모임 상세 조회
   - ###### 이미지 조회
   - ###### 상세정보 조회

- #### 조회수

### 🌐 채팅
- #### 채팅방 생성
    - ###### 그룹채팅,1:1채팅
    
- #### 채팅방 참여
  
- #### 채팅방 및 채팅 조회

- #### 메세지 전송

### 🌐 관리자
- #### 회원관리

- #### 모임관리

- #### 통계관리

- #### 신고 및 제재 관리

- #### 콘텐츠 관리

## Stack

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">

[//]: # (스프링 관련)
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
<img src="https://img.shields.io/badge/JSON Web Tokens-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white">

<img src="https://img.shields.io/badge/Spring data jpa-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">

<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">

[//]: # (데이터베이스 관련)
<img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white">


[//]: # (깃 관련)
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

[//]: # (노션)
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white">

[//]: # (테스트 관련)
<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white">

[//]: # (인텔리제이)
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white">

[//]: # (채팅 관련)
<img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=RabbitMQ&logoColor=white">
<img src="https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white">

[//]: # (배포 자동화 관련)
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">
<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"> 

[//]: # (호스팅)
<img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white">
<img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white">
<img src="https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white">



## Architecture


### Package structure


```
├─main
│  ├─java
│  │  └─com
│  │      └─runto
│  │          ├─domain
│  │          │  ├─admin
│  │          │  │  ├─api
│  │          │  │  ├─application
│  │          │  │  ├─dao
│  │          │  │  ├─dto
│  │          │  │  └─type
│  │          │  ├─chat
│  │          │  │  ├─api
│  │          │  │  ├─application
│  │          │  │  ├─config
│  │          │  │  ├─dao
│  │          │  │  ├─domain
│  │          │  │  ├─dto
│  │          │  │  ├─exception
│  │          │  │  └─type
│  │          │  ├─common
│  │          │  ├─coupon
│  │          │  │  ├─api
│  │          │  │  ├─application
│  │          │  │  ├─dao
│  │          │  │  ├─domain
│  │          │  │  ├─dto
│  │          │  │  ├─facade
│  │          │  │  └─type
│  │          │  ├─email
│  │          │  │  ├─application
│  │          │  │  └─exception
│  │          │  ├─gathering
│  │          │  │  ├─api
│  │          │  │  ├─application
│  │          │  │  ├─dao
│  │          │  │  ├─domain
│  │          │  │  ├─dto
│  │          │  │  ├─exception
│  │          │  │  └─type
│  │          │  ├─image
│  │          │  │  ├─api
│  │          │  │  ├─application
│  │          │  │  ├─dao
│  │          │  │  ├─domain
│  │          │  │  ├─dto
│  │          │  │  ├─exception
│  │          │  │  └─type
│  │          │  └─user
│  │          │      ├─api
│  │          │      ├─application
│  │          │      ├─dao
│  │          │      ├─domain
│  │          │      │  └─report
│  │          │      ├─dto
│  │          │      ├─excepction
│  │          │      └─type
│  │          ├─global
│  │          │  ├─config
│  │          │  ├─exception
│  │          │  ├─security
│  │          │  │  ├─detail
│  │          │  │  ├─dto
│  │          │  │  ├─filter
│  │          │  │  ├─oauth2
│  │          │  │  └─util
│  │          │  └─utils
│  │          └─test_api
│  └─resources
│      └─templates
└─test


```

## Erd

![image](https://github.com/user-attachments/assets/f27a4daf-81f5-4e27-941e-7069d5e9d500)

## Member



| 이름  | 역할         | Github |
|-----|------------|---|
| 이수연 | 실시간 채팅 | https://github.com/lsy28901 |
| 유도경 | 모임 관리 | https://github.com/DokyungYou |
| 유종화 | 관리자 페이지 | https://github.com/YOOJONGHWA |
| 임승진 | 보안,서버 | https://github.com/TestSeung |
