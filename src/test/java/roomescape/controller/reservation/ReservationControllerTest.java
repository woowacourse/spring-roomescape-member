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
import roomescape.dto.reservation.AdminReservationRequest;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.time.TimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql", "/test_member.sql"})
public class ReservationControllerTest {

    @Autowired
    private ReservationController reservationController;

    @Autowired
    private ReservationTimeController timeController;

    @Autowired
    private ReservationThemeController themeController;

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
        timeController.createTime(new TimeRequest(LocalTime.parse("10:00")));
        themeController.createTheme(new ThemeRequest("name", "desc", "thumb"));
        reservationController.createAdminReservation(
                new AdminReservationRequest(LocalDate.parse("2025-01-01"), 1L, 1L, 1L));

        // when & then
        RestAssured.given().log().all()
                .cookie("token", TestUtil.getMemberToken())
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }


    @DisplayName("유저가 예약을 추가한다.")
    @Test
    void insertByUser() {
        // given
        timeController.createTime(new TimeRequest(LocalTime.parse("10:00")));
        themeController.createTheme(new ThemeRequest("name", "desc", "thumb"));

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L))
                .cookie("token", TestUtil.getMemberToken())
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("관리자가 예약을 추가한다.")
    @Test
    void insertByAdmin() {
        // given
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
        RestAssured.given().log().all()
                .cookie("token", TestUtil.getAdminUserToken())
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("날짜가 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidDate() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .cookie("token", TestUtil.getMemberToken())
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
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-04-30");
        params.put("timeId", "시간 선택");
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .cookie("token", TestUtil.getMemberToken())
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("시간이 입력되지 않았습니다."));
    }

    @DisplayName("테마가 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidThemeId() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2024-04-30");
        params.put("timeId", 1);
        params.put("themeId", "테마 선택");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .cookie("token", TestUtil.getMemberToken())
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("테마가 입력되지 않았습니다."));
    }

    @DisplayName("특정 조건에 대한 예약을 조회한다.")
    @Test
    @Sql(scripts = {"/test_schema.sql", "/test_reservation_search.sql", "/test_member.sql"})
    void findByCondition() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", TestUtil.getAdminUserToken())
                .param("themeId", 1L)
                .param("memberId", 1L)
                .param("dateFrom", LocalDate.now().minusDays(7).toString())
                .param("dateTo", LocalDate.now().minusDays(1).toString())
                .when().get("/reservations/search")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(7));
    }
}
