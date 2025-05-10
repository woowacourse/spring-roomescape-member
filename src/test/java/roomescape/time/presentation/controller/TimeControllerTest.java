package roomescape.time.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static roomescape.testFixture.Fixture.MEMBER_1;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_2;
import static roomescape.testFixture.Fixture.THEME_1;
import static roomescape.testFixture.Fixture.createTokenByMemberId;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.AbstractRestDocsTest;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.repository.dto.TimeDataWithBookingInfo;
import roomescape.testFixture.JdbcHelper;
import roomescape.time.domain.ReservationTime;

class TimeControllerTest extends AbstractRestDocsTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE members");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }


    @DisplayName("모든 time 조회 api")
    @Test
    void getAllTimesApiTest() {
        // given
        JdbcHelper.insertReservationTimes(jdbcTemplate, RESERVATION_TIME_1, RESERVATION_TIME_2);

        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);
        String token = createTokenByMemberId(jwtTokenProvider, memberId);

        // when & then
        givenWithDocs("times-getAll")
                .cookie("token", token)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("시간 추가 api")
    @Test
    void createTimeApiTest() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);
        String token = createTokenByMemberId(jwtTokenProvider, memberId);

        // when & then
        givenWithDocs("times-add")
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("id로 time 삭제 api")
    @Test
    void deleteTime() {
        // given
        ReservationTime reservationTime = RESERVATION_TIME_1;
        JdbcHelper.insertReservationTime(jdbcTemplate, reservationTime);

        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);
        String token = createTokenByMemberId(jwtTokenProvider, memberId);

        // when & then
        Long id = reservationTime.getId();
        givenWithDocs("times-deleteById")
                .cookie("token", token)
                .when().delete("/times/" + id)
                .then().log().all()
                .statusCode(204);

        assertThat(getTimesCount()).isEqualTo(0);
    }

    @DisplayName("예약이 존재하는 시간은 삭제 불가")
    @Test
    void cannotDeleteTime_when_hasReservation() {
        // given
        ReservationTime reservationTime = RESERVATION_TIME_1;
        JdbcHelper.insertReservationTime(jdbcTemplate, reservationTime);
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);
        JdbcHelper.insertReservationOnly(jdbcTemplate, RESERVATION_1);

        String payload = String.valueOf(memberId);
        String token = jwtTokenProvider.createToken(payload, Role.USER);

        // when & then
        Long timeId = reservationTime.getId();
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/times/" + timeId)
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("테마와 날짜 선택 후 예약 가능한 시간 조회 요청")
    @Test
    void getTimesWithBookingInfo() {
        // given
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertReservationTimes(jdbcTemplate, RESERVATION_TIME_1, RESERVATION_TIME_2);
        long memberId = JdbcHelper.insertMemberAndGetKey(jdbcTemplate, MEMBER_1);
        JdbcHelper.insertReservationOnly(jdbcTemplate, RESERVATION_1);

        String token = createTokenByMemberId(jwtTokenProvider, memberId);

        // when & then
        String date = RESERVATION_1.getReservationDate().toString();
        List<TimeDataWithBookingInfo> timesData =
                RestAssured.given(documentationSpec)
                        .filter(document("times-bookingStatus",
                                queryParameters(
                                        parameterWithName("themeId").description("시작 날짜"),
//                                                .attributes(key("defaultValue").value("일주일 전 (7일 전)")),
                                        parameterWithName("date").description("종료 날짜")
//                                                .attributes(key("defaultValue").value("어제 (1일 전)")),
                                )
                        ))
                        .cookie("token", token)
                        .when().get(String.format("/times/booking-status?date=%s&themeId=%d", date, THEME_1.getId()))
                        .then().log().all()
                        .statusCode(200)
                        .extract()
                        .body().jsonPath().getList(".", TimeDataWithBookingInfo.class);

        long bookedCount = timesData.stream()
                .filter(TimeDataWithBookingInfo::alreadyBooked)
                .count();

        assertAll(
                () -> assertThat(timesData).hasSize(2),
                () -> assertThat(bookedCount).isEqualTo(1)
        );
    }

    private int getTimesCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);
    }
}
