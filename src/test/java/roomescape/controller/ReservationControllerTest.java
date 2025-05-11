package roomescape.controller;

import static org.hamcrest.Matchers.containsString;
import static roomescape.controller.ApiTestFixture.createReservationTime;
import static roomescape.controller.ApiTestFixture.createTheme;
import static roomescape.controller.ApiTestFixture.loginAndGetToken;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {
    private String token;

    @BeforeEach
    void setUp() {
        createReservationTime();
        createTheme();
        token = loginAndGetToken();
    }

    @Test
    @DisplayName("예약을 생성하면 201을 반환한다.")
    void test0() {

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", "2222-02-02");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
        ;
    }

    @Test
    @DisplayName("필드값 null 검증")
    void test1() {

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", null);
        params.put("date", "2025-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400).body(containsString("[ERROR]"))
        ;
    }

    @Test
    @DisplayName("과거 예약을 생성하면 예외 처리한다. - 1일 전")
    void test2() {

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", String.valueOf(LocalDate.now().minusDays(1)));
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400).body(containsString("[ERROR] 과거 예약은 불가능합니다."))
        ;
    }

    @Test
    @DisplayName("과거 예약을 생성하면 예외 처리한다. - 1시간 전")
    void test3() {

        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("HH:mm")));

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(timeParams)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> reservationParams = new HashMap<>();
        reservationParams.put("memberId", 1);
        reservationParams.put("date", String.valueOf(LocalDate.now()));
        reservationParams.put("timeId", 1);
        reservationParams.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400).body(containsString("[ERROR] 과거 예약은 불가능합니다."))
        ;
    }

    @Test
    @DisplayName("중복 예약을 생성하면 예외 처리한다.")
    void test4() {

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", "2222-02-02");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        params.replace("name", "벡터");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR] Reservation already exists"))
        ;
    }


}
