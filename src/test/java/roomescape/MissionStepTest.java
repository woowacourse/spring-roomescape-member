package roomescape;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.entity.Member;
import roomescape.auth.entity.Role;
import roomescape.global.infrastructure.JwtTokenProvider;
import roomescape.reservation.controller.ReservationController;
import roomescape.reservation.service.dto.response.ReservationResponse;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {
    private final Member admin = new Member(1L, "어드민", Role.ADMIN.name(), "admin@test.com", "1234");
    private final Member user = new Member(2L, "유저", Role.USER.name(), "user@test.com", "1234");
    private String adminToken;
    private String userToken;
    private final long timeId = 1L;
    private final long themeId = 1L;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        // members
        jdbcTemplate.update("INSERT INTO member ( id, name, role, email, password ) VALUES ( ?, ?, ?, ?, ? )", admin.getId(), admin.getName(), admin.getRole().name(), admin.getEmail(), admin.getPassword());
        jdbcTemplate.update("INSERT INTO member ( id, name, role, email, password ) VALUES ( ?, ?, ?, ?, ? )", user.getId(), user.getName(), user.getRole().name(), user.getEmail(), user.getPassword());
        adminToken = jwtTokenProvider.createToken(admin);
        userToken = jwtTokenProvider.createToken(user);

        // time & theme
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", timeId, LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) VALUES (?, ?, ?, ?)", themeId, "theme", "hello", "hi");
    }

    @DisplayName("JdbcTemplate을 테스트용 데이터베이스와 연결할 수 있다.")
    @Test
    void 사단계() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("TEST-DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("어드민 계정으로 /admin API 접근 시, 어드민 메인 화면을 렌더링할 수 있다.")
    @Test
    void renderAdminMain() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("유저 계정으로 /admin API 접근 시, 에러 응답(403)을 받는다.")
    @Test
    void requestAdminMainWithUserToken() {
        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("어드민 계정으로 /admin/reservation API 요청시, 성공 응답을 받을 수 있다.")
    @Test
    void requestAdminReservation() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("GET /reservations API는 권한에 상관없이 성공 응답을 받을 수 있다.")
    @Test
    void requestAllReservationsWithoutAuthToken() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @DisplayName("로그인 유저는 예약을 정상적으로 생성, 조회, 삭제할 수 있다.")
    @Test
    void loginUserCanCRDReservation() {
        LocalDate now = LocalDate.now();
        LocalDate reservationDate = now.plusDays(1);

        Map<String, Object> params = new HashMap<>();
        params.put("date", reservationDate);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        RestAssured.given().log().all()
                .cookie("token", userToken)
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
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @DisplayName("생성되어있는 예약 내역을 조회할 수 있다.")
    @Test
    void selectCreatedReservations() {
        LocalDate now = LocalDate.now();
        LocalDate reservationDate = now.plusDays(1);

        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", user.getId(), reservationDate, timeId, themeId);

        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
    }

    @DisplayName("로그인 유저는 예약을 생성하고 삭제할 수 있다.")
    @Test
    void 육단계() {
        LocalDate now = LocalDate.now();
        LocalDate reservationDate = now.plusDays(1);

        Map<String, Object> params = new HashMap<>();
        params.put("date", reservationDate);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        RestAssured.given().log().all()
                .cookie("token", userToken)
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
                .statusCode(200);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @DisplayName("예약 가능한 시간을 성공적으로 삭제할 수 있다.")
    @Test
    void createAndDeleteReservationTime() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("로그인한 유저는 예약을 생성할 수 있다.")
    @Test
    void 팔단계() {
        LocalDate now = LocalDate.now();
        LocalDate reservationDate = now.plusDays(1);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", reservationDate.toString());
        reservation.put("timeId", timeId);
        reservation.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", userToken)
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

    @Autowired
    private ReservationController reservationController;

    @DisplayName("ReservationController는 JdbcTemplate에 의존하지 않는다.")
    @Test
    void reservationControllerDoesNotHaveDependencyOnJdbcTemplate() {
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
