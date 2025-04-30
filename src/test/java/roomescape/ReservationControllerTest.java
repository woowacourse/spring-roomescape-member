package roomescape;

import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {
    void Test_ReservationTime_Post() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("필드값 null 검증")
    void test1() {
        Test_ReservationTime_Post();

        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        params.put("date", "2025-08-05");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400).body(containsString("[ERROR]"))
        ;
    }

    @Test
    @DisplayName("과거 예약을 생성하면 예외 처리한다. - 1일 전")
    void test2() {
        Test_ReservationTime_Post();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "띠용");
        params.put("date", String.valueOf(LocalDate.now().minusDays(1)));
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400).body(containsString("[ERROR]"))
        ;
    }

    @Test
    @DisplayName("과거 예약을 생성하면 예외 처리한다. - 1시간 전")
    void test3() {
        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", LocalTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("HH:mm")));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParams)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> reservationParams = new HashMap<>();
        reservationParams.put("name", "띠용");
        reservationParams.put("date", String.valueOf(LocalDate.now()));
        reservationParams.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400).body(containsString("[ERROR]"))
        ;
    }

    @Test
    @DisplayName("중복 예약을 생성하면 예외 처리한다.")
    void test4() {
        Test_ReservationTime_Post();

        Map<String, Object> params = new HashMap<>();
        params.put("name", "띠용");
        params.put("date", "2222-02-02");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        params.replace("name", "벡터");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(containsString("[ERROR]"))
        ;
    }

}
