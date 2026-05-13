package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ReservationRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/truncate.sql", "/mockData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ReservationControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void 전체_예약_조회_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(17));
    }

    @Test
    public void 예약_삭제_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    public void 존재하지_않는_예약_삭제_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/reservations/9999")
                .then().log().all()
                .statusCode(404)
                .body("code", is("RESERVATION_NOT_FOUND"));
    }

    @Test
    public void 예약_생성_API() {
        ReservationRequest reservationRequest = new ReservationRequest("포비", LocalDate.now().plusDays(3), 2L, 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("size()", is(5));
    }

    @Test
    public void 이미_존재하는_예약_생성_API() {
        ReservationRequest reservationRequest = new ReservationRequest("새로운사용자", LocalDate.now().plusDays(3), 10L, 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409)
                .body("code", is("RESERVATION_DUPLICATE"));
    }

    @Test
    public void 잘못된_형식의_날짜_입력시_예외가_발생한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"포비\", \"date\": \"abc\", \"timeId\": 2, \"themeId\": 2}")
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_REQUEST_BODY"));
    }

    @Test
    public void 사용자_이름으로_예약을_조회할_수_있다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "루팡4")
                .when().get("/reservations/user")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(4));
    }
}
