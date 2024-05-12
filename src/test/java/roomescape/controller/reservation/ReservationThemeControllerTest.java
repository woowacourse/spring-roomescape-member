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
import roomescape.dto.member.SignupRequest;
import roomescape.dto.reservation.AdminReservationRequest;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.time.TimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql", "/test_admin_member.sql"})
public class ReservationThemeControllerTest {

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

    @DisplayName("테마 조회 요청 시 200으로 응답한다.")
    @Test
    void themes() {
        // given
        themeController.createTheme(new ThemeRequest("name", "desc", "thumb"));
        themeController.createTheme(new ThemeRequest("name1", "desc1", "thumb1"));

        // when & then
        RestAssured.given().log().all()
                .cookie("token", TestUtil.getAdminUserToken())
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("정상적인 테마 추가 요청 시 201으로 응답한다.")
    @Test
    void insert() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");
        params.put("description", "desc");
        params.put("thumbnail", "thumb");

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .cookie("token", TestUtil.getAdminUserToken())
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .body("name", is("name"))
                .body("description", is("desc"))
                .body("thumbnail", is("thumb"));
    }

    @DisplayName("테마 삭제 요청 시 204로 응답한다.")
    @Test
    void delete() {
        RestAssured.given().log().all()
                .cookie("token", TestUtil.getAdminUserToken())
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("예약이 존재하는 테마는 삭제할 수 없다.")
    @Test
    void invalidDeleteTime() {
        // given
        memberController.createMember(new SignupRequest("email@email.com", "password", "username"));
        timeController.createTime(new TimeRequest(LocalTime.parse("10:00")));
        themeController.createTheme(new ThemeRequest("name", "desc", "thumb"));
        reservationController.createAdminReservation(
                new AdminReservationRequest(LocalDate.parse("2025-01-01"), 1L, 1L, 1L));

        // when & then
        RestAssured.given().log().all()
                .cookie("token", TestUtil.getAdminUserToken())
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400)
                .body("message", is("예약이 존재하는 테마는 삭제할 수 없습니다."));
    }

    @DisplayName("최근 일주일간 가장 많이 예약된 테마를 조회한다.")
    @Test
    @Sql(scripts = {"/test_schema.sql", "/test_weekly_theme.sql"})
    void weeklyBestThemes() {
        RestAssured.given().log().all()
                .when().get("/themes/weeklyBest")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4))
                .body("[0].name", is("test3"))
                .body("[1].name", is("test2"))
                .body("[2].name", is("test1"))
                .body("[3].name", is("test4"));
    }
}
