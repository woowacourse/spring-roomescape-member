package roomescape;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationPolicyTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 과거_날짜_예약_시도_시_400_반환() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");

        Map<String, Object> params = new HashMap<>();
        params.put("userId", 1);
        params.put("date", "2020-01-01");  // 과거 날짜
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", equalTo("지난 날짜는 예약할 수 없습니다. 오늘 이후 날짜를 선택해주세요."));
    }

    @Test
    void 잘못된_입력값_예약_시도_시_400_반환() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", null);  // null userId
        params.put("date", "2030-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", equalTo("사용자 ID는 필수입니다."));
    }

    @Test
    void 예약이_존재하는_시간_삭제_시도_시_409_반환() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");
        insertReservation(1L, 1L, "2030-08-05", 1L);

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(409)
                .body("message", equalTo("해당 시간에 예약이 존재하여 삭제할 수 없습니다."));
    }

    @Test
    void 본인_예약_날짜_변경_성공() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");
        insertReservationTime(2L, "14:00:00");
        insertReservation(1L, 1L, "2030-08-05", 1L);

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2030-09-10");
        params.put("timeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/reservations/1?userId=1")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 타인_예약_변경_시도_시_403_반환() {
        insertUser(1L, "브라운", "brown@test.com");
        insertUser(2L, "홍길동", "hong@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");
        insertReservationTime(2L, "14:00:00");
        insertReservation(1L, 1L, "2030-08-05", 1L);  // userId=1의 예약

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2030-09-10");
        params.put("timeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/reservations/1?userId=2")  // userId=2가 변경 시도
                .then().log().all()
                .statusCode(403)
                .body("message", equalTo("본인의 예약만 변경할 수 있습니다."));
    }

    @Test
    void 존재하지_않는_예약_변경_시도_시_404_반환() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2030-09-10");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/reservations/999?userId=1")  // 존재하지 않는 예약 ID
                .then().log().all()
                .statusCode(404)
                .body("message", equalTo("존재하지 않는 예약입니다."));
    }

    @Test
    void 과거_날짜로_예약_변경_시도_시_400_반환() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");
        insertReservation(1L, 1L, "2030-08-05", 1L);

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2020-01-01");  // 과거 날짜로 변경 시도
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/reservations/1?userId=1")
                .then().log().all()
                .statusCode(400)
                .body("message", equalTo("지난 날짜로 변경할 수 없습니다. 오늘 이후 날짜를 선택해 주세요."));
    }

    @Test
    void 이미_예약된_시간으로_변경_시도_시_409_반환() {
        insertUser(1L, "브라운", "brown@test.com");
        insertUser(2L, "홍길동", "hong@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");
        insertReservation(1L, 1L, "2030-08-05", 1L);  // userId=1, 10:00
        insertReservation(2L, 1L, "2030-09-10", 1L);  // userId=2, 10:00 (타겟 날짜 이미 예약)

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2030-09-10");
        params.put("timeId", 1);  // 이미 예약된 날짜+시간+테마

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/reservations/1?userId=1")
                .then().log().all()
                .statusCode(409)
                .body("message", equalTo("선택하신 날짜·시간·테마에 이미 예약이 있습니다. 다른 시간을 선택해 주세요."));
    }

    @Test
    void 과거_예약_변경_시도_시_400_반환() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");
        insertReservationTime(2L, "14:00:00");
        // 과거 날짜로 예약 강제 삽입
        insertReservation(1L, 1L, "2020-01-01", 1L);

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2030-09-10"); // 변경하려는 날짜는 미래라도
        params.put("timeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/reservations/1?userId=1")
                .then().log().all()
                .statusCode(400)
                .body("message", equalTo("지난 예약은 변경하거나 취소할 수 없습니다."));
    }

    @Test
    void 과거_예약_취소_시도_시_400_반환() {
        insertUser(1L, "브라운", "brown@test.com");
        insertTheme(1L, "테마명");
        insertReservationTime(1L, "10:00:00");
        insertReservation(1L, 1L, "2020-01-01", 1L);

        RestAssured.given().log().all()
                .when().delete("/reservations/1?userId=1")
                .then().log().all()
                .statusCode(400)
                .body("message", equalTo("지난 예약은 변경하거나 취소할 수 없습니다."));
    }

    private void insertUser(Long id, String name, String email) {
        jdbcTemplate.update("INSERT INTO users(id, name, email) VALUES (?, ?, ?)", id, name, email);
    }

    private void insertTheme(Long id, String name) {
        jdbcTemplate.update(
                "INSERT INTO theme(id, name, description, thumbnail_image_url) VALUES (?, ?, '설명', 'https://thumbnail.url')",
                id, name);
    }

    private void insertReservationTime(Long id, String startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time(id, start_at) VALUES (?, ?)", id, startAt);
    }

    private void insertReservation(Long userId, Long themeId, String date, Long timeId) {
        jdbcTemplate.update("INSERT INTO reservation(user_id, theme_id, date, time_id) VALUES (?, ?, ?, ?)",
                userId, themeId, date, timeId);
    }
}
