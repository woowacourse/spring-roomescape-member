package roomescape.controller.api.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static roomescape.InitialDataFixture.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class AdminReservationControllerTest {

    @Test
    @DisplayName("관리자 사용자가 예약을 추가한다.")
    void addReservationTest () {
        Map<String, String> params = new HashMap<>();
        params.put("email", MEMBER_2.getEmail().getEmail());
        params.put("password", MEMBER_2.getPassword());

        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().response();

        String token = response.getCookie("token");
        Map<String, Object> reservationInfo = new HashMap<>();
        reservationInfo.put("memberId", MEMBER_2.getId());
        reservationInfo.put("date", LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        reservationInfo.put("themeId", THEME_2.getId());
        reservationInfo.put("timeId", RESERVATION_TIME_3.getId());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservationInfo)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("조건에 맞는 예약을 가져온다.")
    @Test
    void getReservationsTest() {

        Map<String, String> params = new HashMap<>();
        params.put("email", MEMBER_2.getEmail().getEmail());
        params.put("password", MEMBER_2.getPassword());

        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().response();

        String token = response.getCookie("token");

        Map<String, String> conditionParams = new HashMap<>();
        conditionParams.put("dateFrom", "2099-04-01");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .params(conditionParams)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }
}