package roomescape.reservation.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.testFixture.Fixture.MEMBER_1;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_2;
import static roomescape.testFixture.Fixture.RESERVATION_BODY;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.THEME_1;
import static roomescape.testFixture.Fixture.createTokenByMemberId;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.AbstractRestDocsTest;
import roomescape.DatabaseCleaner;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Role;
import roomescape.reservation.presentation.dto.response.ReservationDetailResponse;
import roomescape.testFixture.JdbcHelper;

class ReservationControllerTest extends AbstractRestDocsTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void clean() {
        databaseCleaner.clean();
    }

    @DisplayName("예약 추가 api 호출 시, db에 정상적으로 추가된다.")
    @Test
    public void request_addReservation() {
        JdbcHelper.insertReservationTime(jdbcTemplate, RESERVATION_TIME_1);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);

        String payload = String.valueOf(memberId);
        String token = jwtTokenProvider.createToken(payload, Role.USER);

        givenWithDocs("reservation-add")
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(RESERVATION_BODY)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        assertThat(getReservationsCount()).isEqualTo(1);
    }

    @DisplayName("id로 예약을 삭제하는 api")
    @Test
    void requestDeleteReservation() {
        JdbcHelper.prepareAndInsertReservation(jdbcTemplate, RESERVATION_1);

        Long memberId = RESERVATION_1.getMemberId();
        String token = createTokenByMemberId(jwtTokenProvider, memberId);

        Long id = RESERVATION_1.getId();
        givenWithDocs("reservation-deleteById")
                .cookie("token", token)
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("모든 예약을 조회하는 api")
    @Test
    void getAllReservations() {
        JdbcHelper.insertReservations(jdbcTemplate, RESERVATION_1, RESERVATION_2);

        Long memberId = RESERVATION_1.getMemberId();
        String token = createTokenByMemberId(jwtTokenProvider, memberId);

        List<ReservationDetailResponse> responses =
                givenWithDocs("reservation-getAll")
                        .cookie("token", token)
                        .when().get("/reservations")
                        .then().log().all()
                        .statusCode(200).extract()
                        .jsonPath().getList(".", ReservationDetailResponse.class);

        assertThat(responses.size()).isEqualTo(2);
    }

    @DisplayName("중복된 일시와 테마로 예약 생성 시 예외 발생")
    @Test
    void error_when_duplicateReservation() {
        // given
        JdbcHelper.prepareAndInsertReservation(jdbcTemplate, RESERVATION_1);

        long memberId = RESERVATION_1.getMemberId();
        String token = createTokenByMemberId(jwtTokenProvider, memberId);

        Map<String, Object> reservationBody = Map.of(
                "date", RESERVATION_1.getReservationDate(),
                "timeId", RESERVATION_1.getReservationTime().getId(),
                "themeId", RESERVATION_1.getTheme().getId()
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservationBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("토큰 정보가 없으면 로그인 필요 예외 발생")
    @Test
    void error_when_noLogin() {
        // given
        Map<String, Object> reservationBody = Map.of(
                "date", RESERVATION_1.getReservationDate(),
                "timeId", RESERVATION_1.getReservationTime().getId(),
                "themeId", RESERVATION_1.getTheme().getId()
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(401);
    }

    private int getReservationsCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
    }
}
