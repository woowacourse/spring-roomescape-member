package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialDataFixture.RESERVATION_TIME_1;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.request.ReservationTimeAddRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exceptions.UserException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("중복된 시간을 저장하려고 하면 예외가 발생한다.")
    void saveDuplicatedGetTime() {
        assertThatThrownBy(
                () -> reservationTimeService.addTime(new ReservationTimeAddRequest(RESERVATION_TIME_1.getStartAt()))
        ).isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("예약 가능 시간을 추가하고 id값을 붙여서 응답 DTO를 생성한다.")
    void addGetTime() {
        ReservationTimeAddRequest reservationTimeAddRequest = new ReservationTimeAddRequest(LocalTime.of(15, 0));

        ReservationTimeResponse reservationTimeResponse = reservationTimeService.addTime(reservationTimeAddRequest);

        assertThat(reservationTimeResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("모든 예약 가능 시간을 조회한다.")
    void getTimes() {
        List<ReservationTimeResponse> times = reservationTimeService.findTimes();

        assertThat(times).hasSize(3);
    }

    @Test
    @DisplayName("id에 맞는 예약 가능 시간을 조회한다.")
    void getGetTime() {
        ReservationTimeResponse timeResponse = reservationTimeService.getTime(1L);

        assertThat(timeResponse.startAt()).isEqualTo("09:00");
    }

    @Test
    @DisplayName("id에 맞는 예약 가능 시간을 삭제한다.")
    void deleteGetTime() {
        reservationTimeService.deleteTime(2L);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);

        assertThat(count).isEqualTo(2);
    }
}
