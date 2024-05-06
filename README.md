# ⏰ 매장 예약 후 리뷰 작성 기능을 구현한 프로젝트
: 고객이 원하는 날짜와 시간에 매장 방문을 예약할 수 있고, 
매장 점주가 효율적으로 예약을 관리할 수 있도록 도와주는 예약 시스템입니다.

## ⏱️ 프로젝트 기간 : 2024.04.29 ~ 2024.05.06
<img width="100%" src="https://github.com/ESJung95/store-reservation/assets/155522048/85b1a7d2-1907-478d-9d2e-a91c90746f73"/>

## DB설계, API설계
## DB설계, API설계
<a href="https://docs.google.com/spreadsheets/d/1WqCuN-a8rQgcPnOCRhzdt1_pEoKQ6rxFr-slk-FLhqI/edit?usp=sharing" target="_blank"> 전반적인 설계 </a>

## ⏱️ 주요 기능
[공통 기능]
1. 회원가입
2. 로그인 (jwt 사용)

[사용자 = Customer 기능]
1. 원하는 매장에 방문 예약을 진행하는 기능
2. 매장 검색 기능 (매장의 상세 정보를 확인)
3. 본인이 예약한 예약 정보만 조회하 기능
4. 예약한 매장에서 키오스크를 통해 방문 확인을 진행하는 기능 
(예약 시간 10분 전부터 가능, 예약 시간 20분 초과 시 방문 확인 불가)
5. 매장 방문 후 리뷰 작성, 수정, 삭제하는 기능

[매장 관리자 = Manager 기능]
1. 매장 관리자가 매장 정보를 등록, 수정, 삭제 할 수 있는 기능 
2. 매장으로 예약 요청이 들어오면 승인/거절 할 수 있는 기능
3. 매니저가 등록한 매장 예약만 조회하는 기능

## ⏱️ STACKS
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white"> 
</div>

## ⏱️ 개발 환경
- IDE : IntelliJ Ultimate
- Framework : Spring Boot 3.2.5
- Build Tool : Gradle
- Language : Java 17
- DataBase : MySQL (JPA)
- 외부 라이브러리 사용 : Lombok, JJWT, SpringDoc OpenAPI(Swagger), MySQL Connector/J
  
| 외부 라이브러리      | 사용 방법                                                       |
|-----------------------|----------------------------------------------------------------|
| Lombok                | 반복적인 코드를 자동으로 생성해주기 때문에 롬복을 사용하면 자동으로 게터 및 세터, 생성자 등을 생성할 수 있다. |
| JJWT                  | JWT를 쉽게 생성하고 검증할 수 있다.                             |
| SpringDoc OpenAPI     | Spring Boot 애플리케이션에서 OpenAPI(Swagger) 문서를 자동으로 생성한다. |
| MySQL Connector/J     | MySQL 데이터베이스와의 연결을 위한 JDBC 드라이버로 자바 애플리케이션에서 MySQL 데이터베이스와의 통신을 용이하게 도와준다. |


## ⏱️ ERD
<img width="100%" src="https://github.com/ESJung95/store-reservation/assets/155522048/59e491ee-309a-48c8-bdcb-619001ee6665"/>

## ⏱️ PostMan으로 API TEST (영상 첨부)

## ⏱️ 추가하고 싶은 기능
[공통 기능]
로그 아웃

[Customer 기능]
예약 일정 알림 기능
예약확정이 안된 예약은 삭제
매장별 리뷰 목록 조회
내가 작성한 리뷰 조회
예약 가능 여부 확인

[Manager 기능]
예약이 완료된 날짜나 시간은 제한

## 프로젝트 후 느낀점
1. 테스트 코드 작성을 미루지 말자
2. 예외 처리를 꼼꼼히 하자
3. 데이터의 관계성에 대해서 깊게 공부하기
4. 설계를 최대한 꼼꼼하게 하고, 요구사항을 잘 분석하자
