package roomescape.integration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.service.request.MemberLoginRequest;
import roomescape.service.request.MemberReservationRequest;

/*
 * 테스트 데이터베이스 초기 데이터
 * {ID=1, NAME=브라운, DATE=2024-05-04, TIME={ID=1, START_AT="10:00"}}
 * {ID=2, NAME=엘라, DATE=2024-05-04, TIME={ID=2, START_AT="11:00"}}
 * {ID=3, NAME=릴리, DATE=2023-08-05, TIME={ID=2, START_AT="11:00"}}
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/reset_test_data.sql")
class ReservationIntegrationTest {

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new MemberLoginRequest("user1@gmail.com", "user1"))
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");
    }

    @Test
    @DisplayName("전체 예약 목록을 조회한다.")
    void readReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @Test
    @DisplayName("쿠키에 저장된 토큰을 활용해 예약을 생성한다.")
    void createReservation() {
        LocalDate reservationDate = LocalDate.now().plusDays(1);
        MemberReservationRequest request = new MemberReservationRequest(reservationDate.toString(), 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies("token", token)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", startsWith("/reservations/"))
                .body("date", is(reservationDate.toString()));
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }
}
