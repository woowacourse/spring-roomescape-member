package roomescape.controller;

import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.EMAIL_FIXTURE;
import static roomescape.TestFixture.MEMBER_PARAMETER_SOURCE;
import static roomescape.TestFixture.PASSWORD_FIXTURE;
import static roomescape.TestFixture.createMember;
import static roomescape.TestFixture.createReservationTime;
import static roomescape.TestFixture.createTheme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.MemberReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM member");
    }

    @DisplayName("모든 예약 내역 조회")
    @Test
    void findAllReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("예약 추가 테스트")
    @Test
    void createReservation() {
        // given
        String cookie = createMemberAndReturnCookie();
        MemberReservationRequest memberReservationRequest = createMemberReservationRequest(
                DATE_FIXTURE);
        // then
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .contentType(ContentType.JSON)
                .body(memberReservationRequest)
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("잘못된 양식의 날짜 입력 시 400을 응답한다.")
    @Test
    void invalidDateInput() {
        // given
        SqlParameterSource reservationRequest = new MapSqlParameterSource()
                .addValue("date", "20223-11-09")
                .addValue("timeId", "1")
                .addValue("themeId", "1");
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지나간 시간 예약 시도 시 400을 응답한다.")
    @Test
    void outdatedReservation() {
        // given
        String cookie = createMemberAndReturnCookie();
        MemberReservationRequest memberReservationRequest
                = createMemberReservationRequest(LocalDate.of(2023, 12, 12));
        // when & then
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .contentType(ContentType.JSON)
                .body(memberReservationRequest)
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("중복된 시간 예약 시도 시 400을 응답한다.")
    @Test
    void duplicateReservation() {
        // given
        String cookie = createMemberAndReturnCookie();
        MemberReservationRequest memberReservationRequest = createMemberReservationRequest(
                DATE_FIXTURE);
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .contentType(ContentType.JSON)
                .body(memberReservationRequest)
                .when().post("/reservations")
                .then().log().all();
        // when & then
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .contentType(ContentType.JSON)
                .body(memberReservationRequest)
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("참조키가 존재하지 않음으로 인한 예약 추가 실패 테스트")
    @Test
    void noPrimaryKeyReservation() {
        String cookie = createMemberAndReturnCookie();
        RestAssured.given().log().all()
                .header("cookie", cookie)
                .contentType(ContentType.JSON)
                .body(new MemberReservationRequest(DATE_FIXTURE, 1L, 1L))
                .when().post("/reservations")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예약 취소 성공 테스트")
    @Test
    void deleteReservationSuccess() {
        // given
        String cookie = createMemberAndReturnCookie();
        MemberReservationRequest memberReservationRequest = createMemberReservationRequest(
                DATE_FIXTURE);
        Response reservationResponse = RestAssured.given().log().all()
                .header("cookie", cookie)
                .contentType(ContentType.JSON)
                .body(memberReservationRequest)
                .when().post("/reservations")
                .then().log().all().extract().response();
        long id = reservationResponse.jsonPath().getLong("id");
        // when & then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all().assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("예약 취소 실패 테스트")
    @Test
    void deleteReservationFail() {
        // given
        long invalidId = 0;
        // when & then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + invalidId)
                .then().log().all().assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }

    private String createMemberAndReturnCookie() {
        createMember(jdbcTemplate, MEMBER_PARAMETER_SOURCE);
        LoginRequest loginRequest = new LoginRequest(EMAIL_FIXTURE, PASSWORD_FIXTURE);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];
    }

    private MemberReservationRequest createMemberReservationRequest(LocalDate date) {
        Long timeId = createReservationTime(jdbcTemplate);
        Long themeId = createTheme(jdbcTemplate);
        return new MemberReservationRequest(date, timeId, themeId);
    }
}
