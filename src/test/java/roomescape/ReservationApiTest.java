package roomescape;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/test.sql"})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ReservationApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 어드민_페이지로_접근할_수_있다() {
        RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 어드민이_예약_관리_페이지에_접근한다() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 모든_예약_정보를_반환한다() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(6));
    }

    @Test
    void 존재하지_않는_예약을_삭제할_경우_BAD_REQUEST_반환() {
        RestAssured.given().log().all()
                .when().delete("/reservations/7")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 시간을_추가한뒤_예약을_추가한다() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", LocalDate.now().plusDays(1));
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(7));
    }

    @Test
    void 예약날짜는_null을_받을_수_없다() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", null);
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "abcdefghijk"})
    void 예약자_이름은_빈_값을_받을_수_없다(String name) {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name);
        reservation.put("date", LocalDate.now().plusDays(1));
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 예약_시간_id는_null을_받을_수_없다() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", LocalDate.now().plusDays(1));
        reservation.put("timeId", null);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 과거날짜로_예약을_하면_에러를_반환한다() {
        Map<String, Object> reservationParams = new HashMap<>();
        reservationParams.put("name", "브라운");
        reservationParams.put("date", LocalDate.now().minusDays(10));
        reservationParams.put("timeId", 1);
        reservationParams.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("현재보다 과거의 날짜로 예약 할 수 없습니다."));
    }

    @Test
    void 중복된_시간에_예약을_하면_에러가_발생한다() {
        Map<String, Object> reservationParams = new HashMap<>();
        reservationParams.put("name", "브라운");
        reservationParams.put("date", LocalDate.now().plusDays(10));
        reservationParams.put("timeId", 1);
        reservationParams.put("themeId", 1);

        Map<String, Object> reservationParams2 = new HashMap<>();
        reservationParams2.put("name", "드라고");
        reservationParams2.put("date", LocalDate.now().plusDays(10));
        reservationParams2.put("timeId", 1);
        reservationParams2.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams2)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }
}
