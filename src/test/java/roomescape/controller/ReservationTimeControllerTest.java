package roomescape.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.service.dto.ReservationTimeCreateRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @AfterEach
    void initData() {
        RestAssured.get("/reservations")
                .then().extract().body().jsonPath().getList("id")
                .forEach(id -> RestAssured.delete("/reservations/" + id));

        RestAssured.get("/times")
                .then().extract().body().jsonPath().getList("id")
                .forEach(id -> RestAssured.delete("/times/" + id));

        RestAssured.get("/themes")
                .then().extract().body().jsonPath().getList("id")
                .forEach(id -> RestAssured.delete("/themes/" + id));
    }

    @DisplayName("시간 정보를 추가한다.")
    @Test
    void createReservationTime() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest("10:00"))
                .when().post("/times")
                .then().log().all().statusCode(201).body("id", is(greaterThan(0)));
    }

    @DisplayName("시간 추가 실패 테스트 - 중복 시간 오류")
    @Test
    void createDuplicateTime() {
        //given
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest("10:00"))
                .when().post("/times");

        //when&then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest("10:00"))
                .when().post("/times")
                .then().log().all().statusCode(400);
    }

    @DisplayName("시간 추가 실패 테스트 - 시간 오류")
    @Test
    void createInvalidReservationTime() {
        //given
        String invalidTime = "";

        //when&then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest(invalidTime))
                .when().post("/times")
                .then().log().all()
                .assertThat().statusCode(400).body("message", is("올바르지 않은 시간입니다."));
    }

    @DisplayName("등록된 시간 내역을 조회한다.")
    @Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findAllReservationTime() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all().statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("시간 정보를 id로 삭제한다.")
    @Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void deleteReservationTimeById() {
        RestAssured.given().log().all()
                .when().delete("/times/" + 2)
                .then().log().all()
                .assertThat().statusCode(204);
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .assertThat().body("size()", is(1));
    }

    @DisplayName("시간 삭제 실패 테스트 - 이미 예약이 존재하는 시간 삭제 시도 오류")
    @Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void cannotDeleteReservationTime() {
        RestAssured.given().log().all()
                .when().delete("/times/" + 1)
                .then().log().all()
                .assertThat().statusCode(400).body("message", is("해당 시간에 예약이 존재해서 삭제할 수 없습니다."));
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .assertThat().body("size()", is(2));
    }

    @DisplayName("예약 가능한 시간 조회 테스트")
    @Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findAvailableTime() {
        RestAssured.given().contentType(ContentType.JSON)
                .param("date", "2222-05-04")
                .param("themeId", 1)
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }
}
