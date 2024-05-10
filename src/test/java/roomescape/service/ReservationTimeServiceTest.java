package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.MEMBER_PARAMETER_SOURCE;
import static roomescape.TestFixture.TIME_FIXTURE;
import static roomescape.TestFixture.createMember;
import static roomescape.TestFixture.createReservationTime;
import static roomescape.TestFixture.createTheme;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.request.ReservationTimeWithBookStatusRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ReservationTimeWithBookStatusResponse;
import roomescape.exception.InvalidInputException;
import roomescape.exception.TargetNotExistException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeServiceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ReservationTimeService reservationTimeService;
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


    @DisplayName("존재하는 모든 예약 시간을 반환한다.")
    @Test
    void findAll() {
        assertThat(reservationTimeService.findAll()).isEmpty();
    }

    @DisplayName("날짜와 테마에 따른 예약 가능한 시간을 반환한다.")
    @Test
    void findReservationTimesWithBookStatus() {
        // given
        Long memberId = createMember(jdbcTemplate, MEMBER_PARAMETER_SOURCE);
        Long timeId = createReservationTime(jdbcTemplate);
        Long themeId = createTheme(jdbcTemplate);
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("date", DATE_FIXTURE)
                .addValue("memberId", memberId)
                .addValue("timeId", timeId)
                .addValue("themeId", themeId);
        new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id")
                .execute(parameterSource);
        ReservationTimeWithBookStatusRequest timeRequest
                = new ReservationTimeWithBookStatusRequest(DATE_FIXTURE, themeId);
        // when
        List<ReservationTimeWithBookStatusResponse> timeResponses =
                reservationTimeService.findReservationTimesWithBookStatus(timeRequest);
        // then
        ReservationTimeWithBookStatusResponse timeResponse = timeResponses.get(0);
        assertAll(
                () -> assertThat(timeResponse.id()).isEqualTo(timeId),
                () -> assertThat(timeResponse.startAt()).isEqualTo(TIME_FIXTURE),
                () -> assertThat(timeResponse.booked()).isTrue()
        );
    }

    @DisplayName("예약 시간을 저장한다.")
    @Test
    void save() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(TIME_FIXTURE);
        // when
        ReservationTimeResponse response = reservationTimeService.save(reservationTimeRequest);
        // then
        assertAll(
                () -> assertThat(reservationTimeService.findAll()).hasSize(1),
                () -> assertThat(response.startAt()).isEqualTo(TIME_FIXTURE)
        );
    }

    @DisplayName("중복된 예약 시간을 저장하려 하면 예외가 발생한다.")
    @Test
    void duplicatedTimeSaveThrowsException() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(TIME_FIXTURE);
        reservationTimeService.save(reservationTimeRequest);
        // when & then
        assertThatThrownBy(() -> reservationTimeService.save(reservationTimeRequest))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("이미 존재하는 예약 시간입니다.");
    }

    @DisplayName("예약 시간을 삭제한다.")
    @Test
    void deleteById() {
        // given
        ReservationTimeResponse response = reservationTimeService
                .save(new ReservationTimeRequest(TIME_FIXTURE));
        // when
        reservationTimeService.deleteById(response.id());
        // then
        assertThat(reservationTimeService.findAll()).isEmpty();
    }

    @DisplayName("존재하지 않는 id의 대상을 삭제하려 하면 예외가 발생한다.")
    @Test
    void deleteByNotExistingId() {
        assertThatThrownBy(() -> reservationTimeService.deleteById(-1L))
                .isInstanceOf(TargetNotExistException.class)
                .hasMessage("삭제할 예약 시간이 존재하지 않습니다.");
    }
}
