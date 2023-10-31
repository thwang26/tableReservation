# 매장 예약 서비스
### 과제 시나리오
```text
매장 테이블 예약 서비스 구현
```
### 구현 내용
```text
1. 회원관리
    회원가입 시 사용 목적에 따라 일반회원 (USER), 파트너회원 (PARTNER)로 구분하였습니다.
    회원가입 후 로그인 시 jwt토큰을 발급하여 return해 주고, 요청 권한이 필요한 작업 시
    토큰을 확인하여 어떠한 유저가 요청을 하였는지 검증하였습니다.
    
2. 매장관리
    매장등록 시 네이버 geocoding api를 사용하여 도로명 주소를 통해 위도, 경도 값을 추가로 가져와
    거리별 가까운 매장 정렬을 위해 사용하였습니다. 보안을 위해 geocoding api 키값은 ignore 처리를
    하였습니다.
    
3. 예약
    예약은 매장별 오픈시간과 마감시간 정보를 얻어와 그 사이의 시간에만 예약할 수 있게 하였으며,
    예약은 상태에 따라 크게 요청, 승인, 거부, 사용, 만료로 나누었습니다.
    최초로 예약을 요청하면 예약 발급번호를 특정 코드로 발급해줍니다.
    예약은 요청상태로 있으며, 이후에 매장주인이 승인 또는 거부로 바꿀 수 있습니다.
    사용자가 매장에 도착하여 매장 키오스크에서 예약 발급번호를 입력하면 
    예약 10분전까지 요청하였을 때 사용으로 전환되며 사용 처리됩니다.
    추가적으로, 매일 자정 12시에 전 날 예약 중 취소나 사용이 되지 않은
    날짜가 지난 불분명한 예약은 전부 만료처리를 해주는 스케줄링 기능을 추가하였습니다. 

4. 리뷰
    매장에서 예약을 사용한 이후 해당 예약은 사용처리 되는데
    사용처리 된 예약은 리뷰 작성이 가능합니다.
    리뷰작성과 별점을 기록할 수 있으며 각 리뷰의 별점 평균은 해당 매장의 별점으로 계산됩니다.
    각각의 리뷰에는 매장주인이 답글을 기록할 수 있습니다.
```

### erd테이블
![img](https://i.ibb.co/MhQc1fS/2023-08-07-184642.png)
```text
1. member : 회원정보 저장테이블, 유형에 따라 일반회원, 파트너 회원으로 구분
2. store : 매장 정보를 저장하는 테이블
3. reservation : 예약정보를 저장하는 테이블
4. review : 사용한 예약에 리뷰를 작성하는 테이블 
```

### 구현 api
```text
1. /member
    1. /sign-up : 회원가입기능
    2. /sign-in : 로그인 기능
2. /store
    1. /register-store : 상점등록기능
    2. /get-stores : 상점정보 (가나다순, 별점순, 거리순) 가져오기 기능
    3. /get-store : 상점 상세정보 
3. /reservation
    1. /available-reservation-time : 상점 예약가능시간 가져오기 기능
    2. /request-reservation : 상점 에약기능
    3. /accept-reservation : 상점 예약 승인기능
    4. /cancel-reservation : 상점 예약 거부기능
    5. /use-reservation : 상점 예약 사용기능
    6. /get-reservation : 예약기록 가져오기 기능
4. /review
    1. /write-review : 예약 사용 후 리뷰작성 기능
    2. /write-reply : 리뷰 답글 작성기능
    3. /get-review/{storeId} : 상점에 등록된 리뷰 가져오기 기능
```

### 프로젝트 구조
```text
1. 패키지 구조
기능이 크게 회원, 매장, 예약, 리뷰 총 4개로 나누어져 있으므로
계층형 구조보다는 도메인형 구조가 효율적일 것이라고 판단하여 도메인형 패키지 구조를 선택하였습니다.
각각의 도메인에 controller, domain, repository, service 등의 패키지를 생성하였고
공통적으로 사용되는 config, exception, security 는 global 패키지로 관리하였습니다.

`-- src
    |-- main
    |   |-- generated
    |   |-- java
    |   |   `-- com
    |   |       `-- zerobase
    |   |           `-- springmission
    |   |               |-- SpringMissionApplication.java
    |   |               |-- global
    |   |               |   |-- config
    |   |               |   |   `-- AppConfig.java
    |   |               |   |-- exception
    |   |               |   |   |-- ApiException.java
    |   |               |   |   |-- ErrorCode.java
    |   |               |   |   |-- ErrorResponse.java
    |   |               |   |   |-- GlobalExceptionHandler.java
    |   |               |   |   |-- MemberException.java
    |   |               |   |   |-- ReservationException.java
    |   |               |   |   |-- ReviewException.java
    |   |               |   |   `-- StoreException.java
    |   |               |   `-- security
    |   |               |       |-- JwtAuthenticationFilter.java
    |   |               |       |-- SecurityConfiguration.java
    |   |               |       `-- TokenProvider.java
    |   |               |-- member
    |   |               |   |-- controller
    |   |               |   |   `-- MemberController.java
    |   |               |   |-- domain
    |   |               |   |   `-- Member.java
    |   |               |   |-- dto
    |   |               |   |   |-- SignInRequest.java
    |   |               |   |   `-- SignUp.java
    |   |               |   |-- repository
    |   |               |   |   `-- MemberRepository.java
    |   |               |   |-- service
    |   |               |   |   `-- MemberService.java
    |   |               |   `-- type
    |   |               |       `-- MemberType.java
    |   |               |-- reservation
    |   |               |   |-- controller
    |   |               |   |   `-- ReservationController.java
    |   |               |   |-- domain
    |   |               |   |   `-- Reservation.java
    |   |               |   |-- dto
    |   |               |   |   |-- CancelReservationRequest.java
    |   |               |   |   |-- ReservationInfo.java
    |   |               |   |   |-- ReservationRequest.java
    |   |               |   |   |-- ReservationResponse.java
    |   |               |   |   `-- ReservationTimeResponse.java
    |   |               |   |-- repository
    |   |               |   |   `-- ReservationRepository.java
    |   |               |   |-- service
    |   |               |   |   `-- ReservationService.java
    |   |               |   `-- type
    |   |               |       `-- ReservationType.java
    |   |               |-- review
    |   |               |   |-- controller
    |   |               |   |   `-- ReviewController.java
    |   |               |   |-- domain
    |   |               |   |   `-- Review.java
    |   |               |   |-- dto
    |   |               |   |   |-- ReplyRequest.java
    |   |               |   |   |-- ReplyResponse.java
    |   |               |   |   |-- ReviewRequest.java
    |   |               |   |   `-- ReviewResponse.java
    |   |               |   |-- repository
    |   |               |   |   `-- ReviewRepository.java
    |   |               |   |-- service
    |   |               |   |   `-- ReviewService.java
    |   |               |   `-- type
    |   |               |       `-- RatingType.java
    |   |               `-- store
    |   |                   |-- controller
    |   |                   |   `-- StoreController.java
    |   |                   |-- domain
    |   |                   |   `-- Store.java
    |   |                   |-- dto
    |   |                   |   |-- Coordinate.java
    |   |                   |   |-- RegisterStore.java
    |   |                   |   |-- StoreResponse.java
    |   |                   |   `-- StoreResult.java
    |   |                   |-- repository
    |   |                   |   `-- StoreRepository.java
    |   |                   |-- service
    |   |                   |   |-- GeocodingService.java
    |   |                   |   `-- StoreService.java
    |   |                   `-- type
    |   |                       `-- SortingType.java
    |   `-- resources
    |       |-- application-API-KEY.properties
    |       |-- application.properties
    |       |-- static
    |       `-- templates
    `-- test
        |-- http
        |   |-- member.http
        |   |-- reservation.http
        |   |-- review.http
        |   `-- store.http
        `-- java
            `-- com
                `-- zerobase
                    `-- springmission
                        |-- SpringMissionApplicationTests.java
                        `-- member
                            |-- controller
                            |   `-- MemberControllerTest.java
                            `-- service
                                `-- MemberServiceTest.java

```
