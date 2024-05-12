package roomescape.controller.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.TestUtil;
import roomescape.controller.member.MemberController;
import roomescape.dto.auth.LoginRequest;
import roomescape.dto.member.SignupRequest;
import roomescape.dto.reservation.AdminReservationRequest;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.time.TimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql", "/test_admin_member.sql"})
public class ReservationControllerTest {

    @Autowired
    private ReservationController reservationController;

    @Autowired
    private ReservationTimeController timeController;

    @Autowired
    private ReservationThemeController themeController;

    @Autowired
    private MemberController memberController;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("예약 목록 조회 시 200으로 응답한다.")
    @Test
    void reservations() {
        // given
        memberController.createMember(new SignupRequest("email@email.com", "password", "username"));
        timeController.createTime(new TimeRequest(LocalTime.parse("10:00")));
        themeController.createTheme(new ThemeRequest("name", "desc", "thumb"));
        reservationController.createAdminReservation(
                new AdminReservationRequest(LocalDate.parse("2025-01-01"), 1L, 1L, 1L));

        String accessToken = getAccessToken("email@email.com", "password");

        // when & then
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }


    @DisplayName("유저가 예약을 추가한다.")
    @Test
    void insertByUser() {
        // given
        memberController.createMember(new SignupRequest("email@email.com", "password", "username"));
        timeController.createTime(new TimeRequest(LocalTime.parse("10:00")));
        themeController.createTheme(new ThemeRequest("name", "desc", "thumb"));

        String accessToken = getAccessToken("email@email.com", "password");

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L))
                .cookie("token", accessToken)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("관리자가 예약을 추가한다.")
    @Test
    void insertByAdmin() {
        // given
        memberController.createMember(new SignupRequest("email@email.com", "password", "username"));
        timeController.createTime(new TimeRequest(LocalTime.parse("10:00")));
        themeController.createTheme(new ThemeRequest("name", "desc", "thumb"));

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new AdminReservationRequest(LocalDate.now().plusDays(1), 1L, 1L, 1L))
                .cookie("token", TestUtil.getAdminUserToken())
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("관리자 예약시 유저 정보가 입력되지 않으면 400으로 응답한다.")
    @Test
    void notExistUserInfo() {
        memberController.createMember(new SignupRequest("email@email.com", "password", "username"));

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-04-30");
        params.put("timeId", 1);
        params.put("themeId", 1);
        params.put("memberId", "멤버 선택");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .cookie("token", TestUtil.getAdminUserToken())
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("멤버가 입력되지 않았습니다."));
    }

    @DisplayName("예약 삭제 요청 시 204로 응답한다.")
    @Test
    void deleteById() {
        String accessToken = getAccessToken("admin@email.com", "admin_password");

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("날짜가 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidDate() {
        memberController.createMember(new SignupRequest("email@email.com", "password", "username"));
        String accessToken = getAccessToken("email@email.com", "password");

        Map<String, Object> params = new HashMap<>();
        params.put("date", "");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("날짜가 입력되지 않았습니다."));
    }

    @DisplayName("시간이 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidTimeId() {
        memberController.createMember(new SignupRequest("email@email.com", "password", "username"));
        String accessToken = getAccessToken("email@email.com", "password");

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-04-30");
        params.put("timeId", "시간 선택");
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .cookie("token", accessToken)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("시간이 입력되지 않았습니다."));
    }

    @DisplayName("테마가 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidThemeId() {
        memberController.createMember(new SignupRequest("email@email.com", "password", "username"));
        String accessToken = getAccessToken("email@email.com", "password");

        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-04-30");
        params.put("timeId", 1);
        params.put("themeId", "테마 선택");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .cookie("token", accessToken)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("테마가 입력되지 않았습니다."));
    }

    @DisplayName("특정 조건에 대한 예약을 조회한다.")
    @Test
    @Sql(scripts = {"/test_schema.sql", "/test_reservation_search.sql"})
    void findByCondition() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .param("themeId", 1L)
                .param("memberId", 1L)
                .param("dateFrom", LocalDate.now().minusDays(7).toString())
                .param("dateTo", LocalDate.now().minusDays(1).toString())
                .when().get("/reservations/search")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(7));
    }

    private String getAccessToken(String mail, String password) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(mail, password))
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");
    }
}
