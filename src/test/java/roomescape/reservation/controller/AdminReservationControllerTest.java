package roomescape.reservation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.service.dto.LoginRequest;
import roomescape.reservation.service.dto.AdminReservationRequest;
import roomescape.reservation.service.dto.ReservationRequest;
import roomescape.reservation.service.dto.ReservationTimeCreateRequest;
import roomescape.reservation.service.dto.ThemeRequest;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:truncate-with-admin-and-guest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AdminReservationControllerTest {
    @LocalServerPort
    private int port;

    private String date;
    private long timeId;
    private long themeId;
    private String token;
    private long memberId;

    @BeforeEach
    void init() {
        RestAssured.port = port;

        date = "2222-05-01";
        timeId = (int) RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest("17:46"))
                .when().post("/times")
                .then().extract().response().jsonPath().get("id");

        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themeId = (int) RestAssured.given().contentType(ContentType.JSON).body(themeRequest)
                .when().post("/themes")
                .then().extract().response().jsonPath().get("id");

        token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest("admin123", "admin@email.com"))
                .when().post("/login")
                .then().log().all().extract().cookie("token");

        memberId = 2;
    }

    @DisplayName("예약 추가 성공 테스트")
    @Test
    void createReservation() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(new AdminReservationRequest(date, memberId, timeId, themeId))
                .when().post("/reservations")
                .then().log().all()
                .assertThat().statusCode(201).body("id", is(greaterThan(0)));
    }

    @DisplayName("예약 취소 성공 테스트")
    @Test
    void deleteReservationSuccess() {
        //given
        var id = RestAssured.given().contentType(ContentType.JSON)
                .cookie("token", token)
                .body(new ReservationRequest(date, timeId, themeId))
                .when().post("/reservations")
                .then().extract().body().jsonPath().get("id");

        //when
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/reservations/" + id)
                .then().log().all()
                .assertThat().statusCode(204);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .assertThat().body("size()", is(0));
    }
}
