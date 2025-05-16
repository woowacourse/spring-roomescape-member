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
import roomescape.infra.auth.JwtTokenProcessor;
import roomescape.model.Reservation;
import roomescape.model.user.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProcessor jwtTokenProcessor;
    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        adminToken = jwtTokenProcessor.createToken("asd@asd.com", Role.ADMIN);
        userToken = jwtTokenProcessor.createToken("vec@vec.com", Role.USER);
    }

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

    private void Test_Theme_Post() {
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
                .cookie("loginToken", adminToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 이단계() {
        RestAssured.given().log().all()
                .cookie("loginToken", adminToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .cookie("loginToken", adminToken)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(8)); // 아직 생성 요청이 없으니 Controller에서 임의로 넣어준 Reservation 갯수 만큼 검증하거나 0개임을 확인하세요.
    }

    @Test
    void 삼단계() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2025-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("loginToken", userToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .cookie("loginToken", adminToken)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .cookie("loginToken", adminToken)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .cookie("loginToken", adminToken)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
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

        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id,theme_id) VALUES (?, ?, ?,?)", 1,
                "2025-08-05", 1, 1);

        List<Reservation> reservations = RestAssured.given().log().all()
                .cookie("loginToken", adminToken)
                .when().get("/reservations")
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
        params.put("date", "2025-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("loginToken", userToken)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(count - 1);
    }

    @Test
    void 칠단계() {
        Test_ReservationTime_Post();
        Integer lastTimeIndex = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);

        RestAssured.given().log().all()
                .when().delete("/times/" + lastTimeIndex)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 팔단계() {
        Test_ReservationTime_Post();
        Test_Theme_Post();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("memberId", 1);
        reservation.put("date", "2025-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("loginToken", userToken)
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @Autowired
    private ReservationController reservationController;

    @Test
    void 구단계() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

}
