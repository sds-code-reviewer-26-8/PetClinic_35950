# PetClinic

Spring Boot 기반 PetClinic REST API 프로젝트 (JPA, 인메모리 H2).

## 기술 스택

- Java 17 (Gradle toolchain), Gradle Kotlin DSL (`build.gradle.kts`)
- Spring Boot 4.1.1: Data JPA, WebMVC, Thymeleaf
- springdoc-openapi 3.1.1 (Swagger UI)
- H2 인메모리 DB (+ H2 콘솔, devtools는 `developmentOnly`)
- 테스트: JUnit Platform

## 실행

```
./gradlew bootRun     # http://localhost:8080
./gradlew test
```

- Swagger UI: `/swagger-ui.html`, OpenAPI: `/v3/api-docs`
- H2 콘솔: `/h2-console` (메모리 DB라 재시작 시 데이터 초기화)
- 초기 데이터는 `LoadDatabase`에서 적재

## 디렉터리 구조

루트 패키지: `com.example.petclinic` (`src/main/java/com/example/petclinic/`)

```
PetclinicApplication.java   Spring Boot 진입점
LoadDatabase.java           시작 시 초기 데이터 로딩
controller/                 REST 컨트롤러 (/api/v1/...)
  response/                 응답 DTO (*Dto)
entity/                     JPA 엔티티
repository/                 Spring Data JPA 리포지토리
service/                    비즈니스 로직
src/main/resources/application.yml   설정
src/test/java/.../PetclinicApplicationTests.java
```

## 도메인

| 엔티티 | 설명 |
| --- | --- |
| Person | Owner, Vet의 공통 상위 (firstName, lastName 등) |
| Owner | 소유자 (address, city, telephone) |
| Pet / PetType | 반려동물 / 종류 |
| Visit | 진료 기록 |
| Vet / Speciality / VetSpeciality | 수의사 / 전문 분야 / 둘 사이의 연결 엔티티 |
| Member / Role | 회원 / 권한 |

도메인별로 `XxxController` → `XxxService` → `XxxRepository` → `Xxx` 엔티티 구성이며, 응답은 `controller/response/XxxDto`로 변환해 반환한다.

## 규칙

- 계층 호출 방향: controller → service → repository. 컨트롤러는 엔티티를 직접 반환하지 않고 DTO로 변환한다.
- 새 도메인 추가 시 entity, repository, service, controller, response DTO를 같은 패턴으로 만든다.
