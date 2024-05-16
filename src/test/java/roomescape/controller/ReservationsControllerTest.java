package roomescape.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.service.dto.AdminReservationRequest;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.TokenRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ReservationsControllerTest {
    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        token = RestAssured.given().log().all()
                .body(new TokenRequest("password", "admin@email.com"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().cookies().extract().cookie("token");
    }

    @DisplayName("예약 추가 성공 테스트")
    @Test
    void createReservation() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(new ReservationRequest("2025-01-01", 1, 1))
                .when().post("/reservations")
                .then().log().all()
                .assertThat().statusCode(201)
                .body("id", is(greaterThan(0)));
    }

    @DisplayName("예약 추가 실패 테스트 - 중복 일정 오류")
    @Test
    void createDuplicatedReservation() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationRequest("2222-05-04", 1, 1))
                .when().post("/reservations")
                .then().log().all()
                .assertThat().statusCode(400);
    }

    @DisplayName("예약 추가 실패 테스트 - 일정 오류")
    @Test
    void createInvalidScheduleReservation() {
        //given
        String invalidDate = "2023-10-04";

        //when&then
        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(new ReservationRequest(invalidDate, 1, 1))
                .when().post("/reservations")
                .then().log().all()
                .assertThat().statusCode(400)
                .body("message", is("현재보다 이전으로 일정을 설정할 수 없습니다."));
    }

    @DisplayName("예약 추가 실패 테스트 - 일정 날짜 오류")
    @Test
    void createInvalidScheduleDateReservation() {
        //given
        String invalidDate = "03-04";

        //when&then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new AdminReservationRequest(invalidDate, 1, 1, 1))
                .when().post("/reservations")
                .then().log().all()
                .assertThat().statusCode(400)
                .body("message", is("올바르지 않은 날짜입니다."));
    }

    @DisplayName("모든 예약 내역 조회 테스트")
    @Test
    void findAllReservations() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .assertThat().statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("예약 취소 성공 테스트")
    @Test
    void deleteReservationSuccess() {
        RestAssured.given().log().all()
                .when().delete("/reservations/" + 1)
                .then().log().all()
                .assertThat().statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .assertThat().body("size()", is(0));
    }
}
