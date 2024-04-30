package roomescape.integration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ReservationTimeRequest;

/*
 * 테스트 데이터베이스 초기 데이터
 * {ID=1, START_AT=10:00}
 * {ID=2, START_AT=11:00}
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/reset_test_data.sql")
class ReservationTimeIntegrationTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("전체 시간 목록을 조회한다.")
    void readReservationTimes() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @Test
    @DisplayName("예약 시간을 생성한다.")
    void createReservationTime() {
        ReservationTimeRequest createDto = new ReservationTimeRequest("18:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(createDto)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", startsWith("/times/"))
                .body("startAt", is("18:00"));
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteReservationTime() {
        RestAssured.given().log().all()
                .when().delete("/times/3")
                .then().log().all()
                .statusCode(204);
    }
}
