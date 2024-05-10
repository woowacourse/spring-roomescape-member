package roomescape.admin;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.domain.Role;
import roomescape.member.dto.LoginMember;
import roomescape.member.dto.LoginRequest;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationTimeRequest;
import roomescape.reservation.dto.ReservationTimeResponse;
import roomescape.reservation.dto.ThemeRequest;
import roomescape.reservation.dto.ThemeResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.ReservationTimeService;
import roomescape.reservation.service.ThemeService;
import roomescape.util.ControllerTest;

@DisplayName("관리자 페이지 테스트")
class AdminControllerTest extends ControllerTest {

    @Autowired
    ReservationTimeService reservationTimeService;

    @Autowired
    ThemeService themeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    ReservationTimeResponse reservationTimeResponse;
    ThemeResponse themeResponse;
    String adminToken;

    @BeforeEach
    void setData() {
        String sql = """
            INSERT INTO member(name, email, password, role) VALUES
            ('관리자', 'admin@email.com', 'password', 'ADMIN'),
            ('멤버', 'member@email.com', 'password', 'MEMBER')
        """;
        jdbcTemplate.update(sql);
        reservationTimeResponse = reservationTimeService.create(new ReservationTimeRequest("12:00"));
        themeResponse = themeService.create(new ThemeRequest("name", "description", "thumbnail"));

        adminToken = RestAssured
                .given().log().all()
                .body(new LoginRequest("admin@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract()
                .cookie("token");
    }

    @DisplayName("관리자 메인 페이지 조회에 성공한다.")
    @Test
    void adminMainPage() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200)
                .body(containsString("방탈출 어드민"));
    }

    @DisplayName("관리자 예약 페이지 조회에 성공한다.")
    @Test
    void adminReservationPage() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200)
                .body(containsString("방탈출 예약 페이지"));
    }

    @DisplayName("관리자 예약 생성 시 201을 반환한다.")
    @Test
    void adminPostReservation() {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", "2099-08-05");
        reservation.put("timeId", reservationTimeResponse.id());
        reservation.put("themeId", themeResponse.id());
        reservation.put("memberId", 1);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", adminToken)
                .body(reservation)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("관리자 예약 생성 시, 잘못된 날짜 형식에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "20-12-31", "2020-1-30", "2020-11-0", "-1"})
    void createBadRequest(String date) {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", date);
        reservation.put("timeId", reservationTimeResponse.id());
        reservation.put("themeId", themeResponse.id());
        reservation.put("memberId", 1);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", adminToken)
                .body(reservation)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("관리자 시간 페이지 조회에 성공한다.")
    @Test
    void adminTimePage() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200)
                .body(containsString("시간 관리 페이지"));
    }

    @DisplayName("관리자 테마 페이지 조회에 성공한다.")
    @Test
    void adminThemePage() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200)
                .body(containsString("테마 관리 페이지"));
    }

    @DisplayName("멤버는 관리자 페이지 접근이 불가하다.")
    @Test
    void memberCanNotAccessAdminPage() {
        String memberToken = RestAssured
                .given().log().all()
                .body(new LoginRequest("member@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract()
                .cookie("token");

        RestAssured.given().log().all()
                .cookie("token", memberToken)
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("모든 조건에 따라 멤버 예약을 조회할 수 있다.")
    @Test
    void readReservationByAllFilter() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservations?themeId=1&memberId=2&dateFrom=2024-05-05&dateTo=2024-05-13")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("테마 조건에 따라 멤버 예약을 조회할 수 있다.")
    @Test
    void readReservationByThemeFilter() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservations?themeId=1")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("멤버 조건에 따라 멤버 예약을 조회할 수 있다.")
    @Test
    void readReservationByMemberFilter() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservations?memberId=2")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("시작 날짜 조건에 따라 멤버 예약을 조회할 수 있다.")
    @Test
    void readReservationByDayFromFilter() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservations?dateFrom=2024-05-05")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("끝 날짜 조건에 따라 멤버 예약을 조회할 수 있다.")
    @Test
    void readReservationByDayToFilter() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservations?dateTo=2024-05-05")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("조건이 없어도 멤버 예약을 조회할 수 있다.")
    @Test
    void readReservationByNoneFilter() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("멤버 예약 조회 시, 시작 날짜보다 끝 날짜가 전이면 예외가 발생한다.")
    @Test
    void readReservationByFilterException() {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservations?themeId=1&memberId=2&dateFrom=2024-05-20&dateTo=2024-05-13")
                .then().log().all()
                .statusCode(400);
    }

}
