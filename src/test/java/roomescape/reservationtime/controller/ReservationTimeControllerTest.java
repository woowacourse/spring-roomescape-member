package roomescape.reservationtime.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/create_reservation_time.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationTimeControllerTest {

    @Test
    void 예약_시간_목록을_조회한다() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("startAt", hasItem("10:00:00"));
    }

    @Sql({"/create_reservation_time.sql", "/create_theme.sql"})
    @Test
    void 예약_가능한_시간_목록을_조회한다() {
        RestAssured.given().log().all()
                .contentType("application/json")
                .body("""
                        {
                          "name": "봉구스",
                          "date": "2099-05-06",
                          "timeId": 1,
                          "themeId": 1
                        }
                        """)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .queryParam("date", "2099-05-06")
                .queryParam("themeId", 1)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("startAt", not(hasItem("10:00:00")));
    }

    @Test
    void 날짜와_테마_중_하나만_있으면_400을_응답한다() {
        RestAssured.given().log().all()
                .queryParam("date", "2099-05-06")
                .when().get("/times")
                .then().log().all()
                .statusCode(400);
    }
}
