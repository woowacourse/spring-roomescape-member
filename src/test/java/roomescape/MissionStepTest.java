package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.ReservationController;
import roomescape.dto.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationController reservationController;

    @BeforeEach
    void 시간_테마_넣어주기() {
        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", "10:00");
        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "공포");
        themeParams.put("description", "무서움");
        themeParams.put("thumbnail", "https://roomescape.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParams)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);
    }

    // 1단계
    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(0)); // 아직 생성 요청이 없으니 0개
    }

    @Test
    void 예약_추가_및_삭제() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2030-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(0));
    }

    //2단계
    @Test
    void 데이터베이스_연동() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void DB_조회_API_전환() {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "브라운",
                "2030-08-05", "1", "1");

        List<ReservationResponse> reservationResponses = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList("reservations", ReservationResponse.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(reservationResponses.size()).isEqualTo(count);
    }

    @Test
    void DB_추가_삭제_API_전환() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2030-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    // 3단계
    @Test
    void 시간_관리_API() {
        RestAssured.given().log().all()
                .when().get("/admin/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 예약과_시간_연결() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2030-08-05");
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
                .body("size()", is(1));
    }

    //4단계
    @Test
    void 계층화_리팩터링() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    @Test
    void 예약_가능_시간_조회_정상_흐름_검증() {
        RestAssured.given().log().all()
                .when().get("/themes/1/reservation-times?date=2030-08-05")
                .then().log().all()
                .statusCode(200)
                .body("[0].available", is(true));

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2030-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/themes/1/reservation-times?date=2030-08-05")
                .then().log().all()
                .statusCode(200)
                .body("[0].available", is(false));
    }

    @Test
    void 빈_이름으로_예약을_생성하면_실패한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "");
        params.put("date", "2030-05-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_INPUT"))
                .body("message", is("예약자 이름은 필수입니다."));
    }

    @Test
    void 잘못된_형식으로_예약을_생성하면_실패한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "맥스");
        params.put("date", "2030.05.05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_FORMAT"))
                .body("message", is("입력 형식이 올바르지 않습니다. 날짜(yyyy-MM-dd)와 시간(HH:mm) 형식을 확인해주세요."));
    }

    @Test
    void 예약_시간_없이_시간을_생성하면_실패한다() {
        Map<String, Object> params = new HashMap<>();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("admin/times")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_INPUT"))
                .body("message", is("예약 시간은 필수입니다."));
    }

    @Test
    void 테마_이름_없이_테마를_생성하면_실패한다(){
        Map<String, Object> params = new HashMap<>();
        params.put("name","");
        params.put("description", "무서움");
        params.put("thumbnail", "https://roomescape.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_INPUT"))
                .body("message", is("테마 이름은 필수입니다."));
    }

    @Test
    void 중복_예약을_생성하면_실패한다(){
        Map<String, Object> params = new HashMap<>();
        params.put("name", "맥스");
        params.put("date", "2030-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> duplicateParams = new HashMap<>();
        duplicateParams.put("name", "피노");
        duplicateParams.put("date", "2030-08-05");
        duplicateParams.put("timeId", 1);
        duplicateParams.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(duplicateParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409)
                .body("code", is("DUPLICATE_RESERVATION"))
                .body("message", is("이미 존재하는 예약입니다."));
    }

    @Test
    void 지난_날짜로_예약을_생성하면_실패한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "맥스");
        params.put("date", "2020-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422)
                .body("code", is("PAST_RESERVATION"))
                .body("message", is("지난 날짜 또는 시간은 예약할 수 없습니다."));
    }

    @Test
    void 예약이_존재하는_시간을_삭제하면_실패한다(){
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "맥스");
        reservation.put("date", "2030-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(409)
                .body("code", is("RESOURCE_IN_USE"))
                .body("message", is("예약이 존재해 삭제할 수 없습니다"));
    }

    @Test
    void 존재하지_않는_예약시간으로_예약을_생성하면_실패한다(){
        Map<String, Object> params = new HashMap<>();
        params.put("name", "맥스");
        params.put("date", "2030-08-05");
        params.put("timeId", 9999);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404)
                .body("code", is("NOT_FOUND"))
                .body("message", is("존재하지 않는 예약 시간입니다."));    }

    @Test
    void 존재하지_않는_테마로_예약을_생성하면_실패한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "맥스");
        params.put("date", "2030-08-05");
        params.put("timeId", 1);
        params.put("themeId", 9999);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404)
                .body("code", is("NOT_FOUND"))
                .body("message", is("존재하지 않는 테마입니다."));
    }

    @Test
    void 중복된_예약_시간을_생성하면_실패한다(){
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(409)
                .body("code", is("DUPLICATE_RESERVATION_TIME"))
                .body("message", is("이미 존재하는 예약시간입니다."));
    }

    @Test
    void 중복된_테마_이름으로_테마를_생성하면_실패한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "공포");
        params.put("description", "무서운 테마");
        params.put("thumbnail", "https://roomescape.com/other");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(409)
                .body("code", is("DUPLICATE_THEME"))
                .body("message", is("이미 존재하는 테마 이름입니다."));
    }

}
