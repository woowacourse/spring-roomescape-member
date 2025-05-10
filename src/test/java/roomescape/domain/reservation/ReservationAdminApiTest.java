package roomescape.domain.reservation;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.service.JwtManager;
import roomescape.domain.reservation.utils.JdbcTemplateUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservationAdminApiTest {

    private static String adminToken;
    private static String userToken;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @LocalServerPort
    private int port;

    @BeforeAll
    static void setUp(@Autowired final JwtManager jwtManager) {
        final User admin = new User(1L, new Name("꾹"), "tizm@naver.com", "1234", Roles.ADMIN);
        final User user = new User(1L, new Name("꾹"), "user@naver.com", "1234", Roles.USER);

        adminToken = jwtManager.createToken(admin);
        userToken = jwtManager.createToken(user);
    }

    @BeforeEach
    void init() {
        RestAssured.port = port;
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }

    @DisplayName("어드민 페이지로 접근할 수 있다.")
    @Test
    void reservationTest1() {
        RestAssured.given()
                .cookie("token", adminToken)
                .log()
                .all()
                .when()
                .get("/admin")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @DisplayName("권한이 없다면 403 상태 코드를 반환한다.")
    @Test
    void reservationTest2() {
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/admin")
                .then()
                .log()
                .all()
                .statusCode(403);

    }

    @DisplayName("어드민이 예약 관리 페이지에 접근한다.")
    @Test
    void adminReservationTest1() {
        RestAssured.given()
                .cookie("token", adminToken)
                .log()
                .all()
                .when()
                .get("/admin/reservation")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @DisplayName("권한이 없다면 예약 관리 페이지에 접근할 수 없으며 403 에러 코드를 반환한다.")
    @Test
    void adminReservationTest2() {
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/admin/reservation")
                .then()
                .log()
                .all()
                .statusCode(403);
    }

    @DisplayName("일반 유저가 어드민 페이지에 접근하면 403 예외가 발생한다.")
    @Test
    void userAccessAdminPage_forbidden() {
        // given
        // when & then
        RestAssured.given()
                .cookie("token", userToken)
                .log()
                .all()
                .when()
                .get("/admin")
                .then()
                .log()
                .all()
                .statusCode(403);

        RestAssured.given()
                .cookie("token", userToken)
                .log()
                .all()
                .when()
                .get("/admin/reservation")
                .then()
                .log()
                .all()
                .statusCode(403);
    }
}
