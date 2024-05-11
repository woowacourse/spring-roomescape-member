package roomescape.controller.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.AuthenticationProvider;

import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate-data.sql")
public class ReservationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자는 예약 조회, 삭제를 할 수 있다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void findDeleteReservationWithAdmin_Success() {
        String token = AuthenticationProvider.loginAdmin();
        Map<String, Object> reservation = Map.of(
                "date", LocalDate.now().plusDays(1L).toString(),
                "timeId", 1,
                "themeId", 1
        );

        String location = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract().header("Location");

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        String reservationId = location.substring(location.lastIndexOf("/") + 1);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("일반 사용자는 예약 조회, 삭제를 할 수 없다.")
    @Sql(scripts = {"/truncate-data.sql", "/member-data.sql"})
    void findDeleteReservationWithMember_Failure() {
        String token = AuthenticationProvider.loginMember();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(403);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(403);
    }

    @Test
    @DisplayName("일반 사용자는 예약을 추가할 수 있다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void addReservationWithMember_Success() {
        String token = AuthenticationProvider.loginMember();
        Map<String, Object> reservation = Map.of(
                "date", LocalDate.now().plusDays(1L).toString(),
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("로그인 하지 않은 사용자는 예약을 추가할 수 없다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void addReservationWithoutLogin_Failure() {
        Map<String, Object> reservation = Map.of(
                "date", LocalDate.now().plusDays(1L).toString(),
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(401);
    }
}
