package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.domain.Role;
import roomescape.reservation.controller.ReservationController;
import roomescape.reservation.controller.dto.ReservationResponse;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationController reservationController;

    @DisplayName("루트 경로 페이지 요청에 성공하면 200 코드를 반환한다")
    @Test
    void root_request_test() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("어드민 경로 페이지 요청에 성공하면 200 코드를 반환한다")
    @Test
    void admin_request_test() {
        String token = getToken("admin@gmail.com", "a", "어드민", Role.ADMIN);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("예약 경로 페이지 요청에 성공하면 200 코드를 반환한다")
    @Test
    void reservation_request_test() {
        String token = getToken("admin@gmail.com", "a", "어드민", Role.ADMIN);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(16));
    }

    @DisplayName("예약 및 삭제 요청이 200 코드를 반환한다")
    @Test
    void reservation_delete_test() {
        String token = getToken("a@gmail.com", "a", "하루", Role.USER);

        Map<String, String> params = new HashMap<>();
        params.put("date", "2026-08-05");
        params.put("timeId", "6");
        params.put("themeId", "2");
//        params.put("memberId", "1");

        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(17));

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(17));

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(16));
    }

    @DisplayName("데이터 삭제를 성공하면 204 코드를 반환한다")
    @Test
    void reservation_delete_exception() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("연결된 DB 커넥션을 확인한다")
    @Test
    void connection_test() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("전체 Reservation 객체를 조회한다")
    @Test
    void get_reservations_test() {
        jdbcTemplate.update("INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES (?, ?, ?, ?)",
                "2023-08-05",
                "1", "1", "1");

        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(*) from reservation", Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
    }

    @DisplayName("시간 관리 생성, 조회, 삭제 테스트")
    @Test
    void time_test() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:20");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(7));

        RestAssured.given().log().all()
                .when().delete("/times/7")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("Controller에서 JdbcTemplate의 의존을 분리한다")
    @Test
    void jdbcTemplate_inject_test() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    @DisplayName("테마 생성, 조회, 삭제 테스트")
    @Test
    void theme_test() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "레벨1 탈출");
        params.put("description", "우테코 레벨1를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(7));

        RestAssured.given().log().all()
                .when().delete("/themes/7")
                .then().log().all()
                .statusCode(204);
    }

    private String getToken(String email, String password, String name, Role role) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("email", email, "password", password, "name", name, "role", role.name()))
                .when().post("/login")
                .then().log().all()
                .extract()
                .cookie("token");
    }
}
