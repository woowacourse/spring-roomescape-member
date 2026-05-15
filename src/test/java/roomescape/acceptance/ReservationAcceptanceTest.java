package roomescape.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void GET_reservations_목록을_조회한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-05-06", 1L);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(1))
                .body("reservations[0].name", equalTo("브라운"));
    }

    @Test
    void GET_reservations_name_쿼리_파라미터로_본인의_예약만_조회한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-05-01", 1L);
        insertReservation("다른사람", 1L, "2026-05-02", 1L);
        insertReservation("브라운", 1L, "2026-05-03", 1L);

        RestAssured.given().log().all()
                .when().get("/reservations?name=브라운")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(2))
                .body("reservations.name", org.hamcrest.Matchers.everyItem(equalTo("브라운")));
    }

    @Test
    void GET_reservations_name이_빈_문자열이면_400과_메시지를_반환한다() {
        RestAssured.given().log().all()
                .when().get("/reservations?name=")
                .then().log().all()
                .statusCode(400)
                .body("message", equalTo("name은(는) 최소 1자 이상이어야 합니다."));
    }

    @Test
    void GET_reservations_id_단건을_조회한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-05-06", 1L);

        RestAssured.given().log().all()
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(200)
                .body("name", equalTo("브라운"));
    }

    @Test
    void POST_reservations_예약을_생성한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-05-08",
                "themeId", 1,
                "timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", matchesPattern("/reservations/\\d+"));
    }

    @Test
    void POST_reservations_같은_날짜시간테마_중복이면_409과_메시지를_반환한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("기존", 1L, "2026-05-08", 1L);

        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-05-08",
                "themeId", 1,
                "timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409)
                .body("message", equalTo("해당 날짜·시간·테마에 이미 예약이 존재합니다. 다른 날짜·시간·테마를 선택해주세요."));
    }

    @Test
    void POST_reservations_과거_날짜_시간이면_422과_메시지를_반환한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");

        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-05-06",
                "themeId", 1,
                "timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422)
                .body("message", equalTo("예약 일정이 유효하지 않습니다. 예약 날짜와 시간은 현시간 이후여야 합니다."));
    }

    @Test
    void POST_reservations_본문의_date가_형식_오류면_400과_메시지를_반환한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");

        String body = """
                {"name":"브라운","date":"abc","themeId":1,"timeId":1}
                """;

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", equalTo("'date' 값 'abc'은(는) yyyy-MM-dd 형식이어야 합니다."));
    }

    @Test
    void POST_reservations_본문의_name이_누락되면_400과_메시지를_반환한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");

        Map<String, Object> body = Map.of(
                "date", "2026-05-08",
                "themeId", 1,
                "timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", equalTo("name은(는) 필수 입력값입니다."));
    }

    @Test
    void POST_reservations_본문_JSON_문법_오류면_400과_메시지를_반환한다() {
        String brokenBody = "{\"name\":\"브라";

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(brokenBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", equalTo("요청 본문 형식이 올바르지 않습니다."));
    }

    @Test
    void GET_reservations_id_없는_id면_404과_메시지를_반환한다() {
        RestAssured.given().log().all()
                .when().get("/reservations/9999")
                .then().log().all()
                .statusCode(404)
                .body("message", equalTo("예약을(를) 찾을 수 없습니다. id=9999"));
    }

    @Test
    void PUT_reservations_id_본인의_예약을_변경한다() {
        insertTheme(1L, "테마1");
        insertTheme(2L, "테마2");
        insertTime(1L, "10:00");
        insertTime(2L, "11:00");
        insertReservation("브라운", 1L, "2026-06-01", 1L);

        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-06-02",
                "themeId", 2,
                "timeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().put("/reservations/1")
                .then().log().all()
                .statusCode(200)
                .body("date", equalTo("2026-06-02"));
    }

    @Test
    void PUT_reservations_id_이름이_일치하지_않으면_403과_메시지를_반환한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-06-01", 1L);

        Map<String, Object> body = Map.of(
                "name", "다른사람",
                "date", "2026-06-02",
                "themeId", 1,
                "timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().put("/reservations/1")
                .then().log().all()
                .statusCode(403)
                .body("message", equalTo("본인의 예약만 취소 혹은 변경 가능합니다."));
    }

    @Test
    void PUT_reservations_id_과거_예약을_변경하려_하면_422과_메시지를_반환한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-05-01", 1L);

        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-06-02",
                "themeId", 1,
                "timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().put("/reservations/1")
                .then().log().all()
                .statusCode(422)
                .body("message", equalTo("예약 일정이 유효하지 않습니다. 예약 날짜와 시간은 현시간 이후여야 합니다."));
    }

    @Test
    void PUT_reservations_id_새_일정이_과거이면_422과_메시지를_반환한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-06-01", 1L);

        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-05-01",
                "themeId", 1,
                "timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().put("/reservations/1")
                .then().log().all()
                .statusCode(422)
                .body("message", equalTo("예약 일정이 유효하지 않습니다. 예약 날짜와 시간은 현시간 이후여야 합니다."));
    }

    @Test
    void PUT_reservations_id_새_일정이_이미_예약된_슬롯이면_409과_메시지를_반환한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertTime(2L, "11:00");
        insertReservation("브라운", 1L, "2026-06-01", 1L);
        insertReservation("다른사람", 1L, "2026-06-02", 2L);

        Map<String, Object> body = Map.of(
                "name", "브라운",
                "date", "2026-06-02",
                "themeId", 1,
                "timeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().put("/reservations/1")
                .then().log().all()
                .statusCode(409)
                .body("message", equalTo("해당 날짜·시간·테마에 이미 예약이 존재합니다. 다른 날짜·시간·테마를 선택해주세요."));
    }

    @Test
    void DELETE_reservations_id_본인의_예약을_취소한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-05-06", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "브라운"))
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void DELETE_reservations_id_이름이_일치하지_않으면_403과_메시지를_반환한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-05-06", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "다른사람"))
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(403)
                .body("message", equalTo("본인의 예약만 취소 혹은 변경 가능합니다."));
    }

    @Test
    void DELETE_reservations_id_없는_id면_404과_메시지를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "브라운"))
                .when().delete("/reservations/9999")
                .then().log().all()
                .statusCode(404)
                .body("message", equalTo("예약을(를) 찾을 수 없습니다. id=9999"));
    }

    @Test
    void DELETE_reservations_id_본문의_name이_누락되면_400과_메시지를_반환한다() {
        insertTheme(1L, "테마");
        insertTime(1L, "10:00");
        insertReservation("브라운", 1L, "2026-05-06", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of())
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(400)
                .body("message", equalTo("name은(는) 필수 입력값입니다."));
    }

    private void insertTheme(Long id, String name) {
        jdbcTemplate.update(
                "INSERT INTO theme(id, name, description, thumbnail_image_url) VALUES (?, ?, '설명', 'https://thumbnail.url')",
                id, name);
    }

    private void insertTime(Long id, String startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time(id, start_at) VALUES (?, ?)", id, startAt);
    }

    private void insertReservation(String name, Long themeId, String date, Long timeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation(name, theme_id, date, time_id) VALUES (?, ?, ?, ?)",
                name, themeId, date, timeId);
    }
}