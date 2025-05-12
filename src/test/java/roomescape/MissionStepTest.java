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
import roomescape.controller.ApiTestFixture;
import roomescape.controller.rest.AdminReservationController;
import roomescape.model.Reservation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {
    private String token;

    @BeforeEach
    void setup() {
        ApiTestFixture.signUpAdmin("admin@gmail.com", "password", "vector");
        token = ApiTestFixture.loginAndGetToken("admin@gmail.com", "password");
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    private static void Test_Theme_Post() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "Ddyong");
        params.put("description", "살인마가 쫓아오는 느낌");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 일단계() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 이단계() {
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0)); // 아직 생성 요청이 없으니 Controller에서 임의로 넣어준 Reservation 갯수 만큼 검증하거나 0개임을 확인하세요.
    }

    @Test
    void 삼단계() {
        Test_ReservationTime_Post();
        Test_Theme_Post();

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", "2025-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)

                .when().post("admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 사단계() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 오단계() {
        Test_ReservationTime_Post();
        Test_Theme_Post();

        jdbcTemplate.update("INSERT INTO reservation ( date,member_id, time_id,theme_id) VALUES (?, ?, ?,?)",
                "2025-08-05", 1, 1, 1);

        List<Reservation> reservations = RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    void 육단계() {
        Test_ReservationTime_Post();
        Test_Theme_Post();

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", "2025-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    void 칠단계() {
        Test_ReservationTime_Post();

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 팔단계() {
        Test_ReservationTime_Post();
        Test_Theme_Post();

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", "2025-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Autowired
    private AdminReservationController adminReservationController;

    @Test
    void 구단계() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : adminReservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }
        assertThat(isJdbcTemplateInjected).isFalse();
    }
}
