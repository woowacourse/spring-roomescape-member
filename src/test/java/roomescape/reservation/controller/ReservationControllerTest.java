package roomescape.reservation.controller;

import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.domain.Role;
import roomescape.member.dto.LoginMember;
import roomescape.member.dto.LoginRequest;
import roomescape.member.service.MemberService;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationTimeRequest;
import roomescape.reservation.dto.ReservationTimeResponse;
import roomescape.reservation.dto.ThemeRequest;
import roomescape.reservation.dto.ThemeResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.ReservationTimeService;
import roomescape.reservation.service.ThemeService;
import roomescape.util.ControllerTest;

@DisplayName("예약 API 통합 테스트")
class ReservationControllerTest extends ControllerTest {
    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationTimeService reservationTimeService;

    @Autowired
    ThemeService themeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    ReservationTimeResponse reservationTimeResponse;
    ThemeResponse themeResponse;

    @BeforeEach
    void setData() {
        String sql = """ 
                  INSERT INTO member(name, email, password, role)
                  VALUES ('관리자', 'admin@email.com', 'password', 'ADMIN');
                """;
        jdbcTemplate.update(sql);
        reservationTimeResponse = reservationTimeService.create(new ReservationTimeRequest("12:00"));
        themeResponse = themeService.create(new ThemeRequest("name", "description", "thumbnail"));
        reservationService.create(new ReservationRequest(
                        "2099-04-23",
                        reservationTimeResponse.id(),
                        themeResponse.id()
                ),
                new LoginMember(1, "siso", "test@email.com", Role.MEMBER)
        );
    }

    @DisplayName("인기 테마 페이지 조회에 성공한다.")
    @Test
    void popularPage() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200)
                .body(containsString("인기 테마"));
    }

    @DisplayName("예약 조회 시 200을 반환한다.")
    @Test
    void find() {
        //given & when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("예약 생성 시 201을 반환한다.")
    @Test
    void create() {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", "2099-08-05");
        reservation.put("timeId", reservationTimeResponse.id());
        reservation.put("themeId", themeResponse.id());

        String accessToken = RestAssured
                .given().log().all()
                .body(new LoginRequest("admin@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("예약 생성 시, 빈 이름에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "           "})
    void createNameBadRequest(String name) {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name);
        reservation.put("date", "2100-12-01");
        reservation.put("timeId", reservationTimeResponse.id());
        reservation.put("themeId", themeResponse.id());

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 생성 시, 잘못된 날짜 형식에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "20-12-31", "2020-1-30", "2020-11-0", "-1"})
    void createBadRequest(String date) {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", date);
        reservation.put("timeId", reservationTimeResponse.id());
        reservation.put("themeId", themeResponse.id());

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 생성 시, 빈 시간 id에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "           "})
    void createTimeIdBadRequest(String timeId) {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "siso");
        reservation.put("date", "2100-12-01");
        reservation.put("timeId", timeId);
        reservation.put("themeId", themeResponse.id());

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 생성 시, 빈 테마 id에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "           "})
    void createThemeIdBadRequest(String themeId) {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "siso");
        reservation.put("date", "2100-12-01");
        reservation.put("timeId", reservationTimeResponse.id());
        reservation.put("themeId", themeId);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 삭제 시 204를 반환한다.")
    @Test
    void delete() {
        //given
        long id = reservationService.findAllReservations().stream()
                .findFirst().orElseThrow().id();

        //when &then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않은 예약 삭제 시 400를 반환한다.")
    @Test
    void reservationNotFound() {
        //given & when & then
        RestAssured.given().log().all()
                .when().delete("/reservations/6")
                .then().log().all()
                .statusCode(400);
    }
}
