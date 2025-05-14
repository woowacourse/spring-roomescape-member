package roomescape.integration;

import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.entity.AccessToken;
import roomescape.entity.Member;
import roomescape.entity.MemberRole;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminReservationApiIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private AccessToken adminToken;

    @BeforeEach
    void setUpData() {
        String adminName = "moda";
        String adminEmail = "moda_email";
        String adminPassword = "moda_password";

        String memberSetUp = "insert into member (name, email, password, role) values (?, ?, ?, 'ADMIN')";
        String reservationTimeSetUp = "insert into reservation_time (start_at) values ('10:00')";
        String themeSetUp = "insert into theme (name, description, thumbnail) values ('theme_name', 'theme_description', 'theme_thumbnail')";
        String reservationSetUp = "insert into reservation (date, member_id, time_id, theme_id) values ('2025-08-04', 1, 1, 1)";
        jdbcTemplate.update(memberSetUp, adminName, adminEmail, adminPassword);
        jdbcTemplate.update(reservationTimeSetUp);
        jdbcTemplate.update(themeSetUp);
        jdbcTemplate.update(reservationSetUp);

        Member admin = new Member(1L, adminName, adminEmail, adminPassword, MemberRole.ADMIN);
        adminToken = new AccessToken(admin);
    }

    @Test
    @DisplayName("관리자가 전체 예약을 조회한다.")
    void getAllReservations() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", adminToken.getValue())
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("인증 토큰이 없으면 예외가 발생한다.")
    void failIfNotVerified() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("관리자가 아닐 경우 예외가 발생한다.")
    void failIfNotAdmin() {
        Member user = new Member(2L, "modada", "modada_email", "modada_password", MemberRole.USER);
        AccessToken userToken = new AccessToken(user);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", userToken.getValue())
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("관리자가 예약을 검색한다.")
    void filterReservations() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .param("themeId", 1)
                .param("memberId", 1)
                .param("dateFrom", "2025-08-04")
                .param("dateTo", "2025-08-04")
                .cookie("token", adminToken.getValue())
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자가 예약을 생성한다.")
    void postReservation() {
        //given
        String date = "2025-08-05";

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", date);
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);
        reservation.put("memberId", 1);

        //when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", adminToken.getValue())
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(2),
                        "date", is(date),
                        "member.id", is(1),
                        "member.name", is("moda"),
                        "time.id", is(1),
                        "time.startAt", is("10:00"),
                        "theme.id", is(1),
                        "theme.name", is("theme_name")
                );
    }

    @Test
    @DisplayName("관리자가 예약을 삭제한다.")
    void deleteReservations() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", adminToken.getValue())
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("잘못된 id의 예약을 삭제할 경우 예외가 발생한다.")
    void failDeleteReservations() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", adminToken.getValue())
                .when().delete("/admin/reservations/2")
                .then().log().all()
                .statusCode(400);
    }
}
