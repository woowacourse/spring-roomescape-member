package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.config.JwtTokenProvider;
import roomescape.domain.Role;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationRestControllerTest {

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private ReservationService reservationService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자가 모든 예약을 조회한다.")
    void getAll() {
        // given && when
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        // then
        assertThat(reservations).hasSize(10);
    }

    @Test
    @DisplayName("관리자가 필터 옵션을 적용한 모든 예약을 조회한다.")
    void getAll_filter() {
        // given
        String token = tokenProvider.createToken(1L, "관리자", Role.ADMIN.name());
        Map<String, String> cookies = Map.of(JwtTokenProvider.TOKEN_COOKIE_NAME, token);

        // when
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .cookies(cookies)
                .params("memberId", "2",
                        "themeId", "2",
                        "dateFrom", "2024-01-01",
                        "dateTo", "2024-12-31")
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        // then
        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("사용자가 예약을 생성한다.")
    void create() {
        // given
        String token = tokenProvider.createToken(1L, "사용자1", Role.MEMBER.name());
        Map<String, String> cookies = Map.of(JwtTokenProvider.TOKEN_COOKIE_NAME, token);
        Map<String, String> params = Map.of(
                "date", "2099-12-31",
                "timeId", "1",
                "themeId", "1"
        );

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertThat(reservations).hasSize(11);
    }

    @Test
    @DisplayName("관리자가 예약을 생성한다.")
    void adminCreate() {
        // given
        String token = tokenProvider.createToken(1L, "관리자", Role.ADMIN.name());
        Map<String, String> cookies = Map.of(JwtTokenProvider.TOKEN_COOKIE_NAME, token);
        Map<String, String> params = Map.of(
                "date", "2099-12-31",
                "timeId", "1",
                "themeId", "1",
                "memberId", "1"
        );

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertThat(reservations).hasSize(11);
    }

    @Test
    @DisplayName("관리자가 예약을 같은 날짜, 테마, 시간에 중복된 예약을 생성하려고 하면 BAD_REQUEST를 반환한다.")
    void adminCreate_duplicate_badRequest() {
        // given
        String token = tokenProvider.createToken(1L, "관리자", Role.ADMIN.name());
        Map<String, String> cookies = Map.of(JwtTokenProvider.TOKEN_COOKIE_NAME, token);
        Map<String, String> params = Map.of(
                "date", "2024-05-10",
                "timeId", "1",
                "themeId", "1",
                "memberId", "1"
        );

        // when && then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("관리자가 해당 id의 예약을 삭제한다.")
    void delete() {
        // given
        String token = tokenProvider.createToken(1L, "관리자", Role.ADMIN.name());
        Map<String, String> cookies = Map.of(JwtTokenProvider.TOKEN_COOKIE_NAME, token);

        // when
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertThat(reservations).hasSize(9);
    }
}
