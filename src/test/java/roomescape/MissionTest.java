package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.JwtProvider;
import roomescape.controller.ReservationController;
import roomescape.domain.Member;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MissionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationController reservationController;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MemberRepository memberRepository;

    private Member admin;

    private String adminLoginToken;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("""
                SET REFERENTIAL_INTEGRITY FALSE;
                TRUNCATE TABLE reservation;
                ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;
                TRUNCATE TABLE reservation_time;
                ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1;
                TRUNCATE TABLE theme;
                ALTER TABLE theme ALTER COLUMN id RESTART WITH 1;
                TRUNCATE TABLE member;
                ALTER TABLE member ALTER COLUMN id RESTART WITH 1;
                SET REFERENTIAL_INTEGRITY TRUE;
                """);
        admin = memberRepository.save(new Member("admin", "admin@admin.com", "password", "admin"));
        adminLoginToken = jwtProvider.createToken(admin);
    }

    @Test
    void 일단계() {
        RestAssured.given()
                .cookie("token", adminLoginToken).log().all()
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 이단계() {
        RestAssured.given()
                .cookie("token", adminLoginToken).log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 삼단계() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> params2 = new HashMap<>();
        params2.put("name", "name");
        params2.put("description", "des");
        params2.put("thumbnail", "thumb");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params2)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> params3 = new HashMap<>();
        params3.put("date", LocalDate.now().plusDays(20));
        params3.put("timeId", "1");
        params3.put("themeId", "1");

        RestAssured.given()
                .cookie("token", adminLoginToken).log().all()
                .contentType(ContentType.JSON)
                .body(params3)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
        RestAssured.given().log().all()
                .when().get("/reservations")
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
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "name", "desc", "thumb");
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                admin.id(), LocalDate.now().plusDays(20), "1", "1");

        final List<ReservationResponse> response = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(response).hasSize(count);
    }

    @Test
    void 육단계() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "name", "desc", "thumb");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(20));
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given()
                .cookie("token", adminLoginToken).log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    void 칠단계() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "name", "desc", "thumb");

        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .queryParam("themeId", 1)
                .queryParam("date", "2025-05-01")
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
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "name", "desc", "thumb");

        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", LocalDate.now().plusDays(1));
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given()
                .cookie("token", adminLoginToken).log().all()
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
