package roomescape.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.request.ReservationTimeRequest;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.ReservationTimeFixture.reservationTimeFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @DisplayName("예약 시간 추가 테스트")
    @Test
    void createReservationTime() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(reservationTimeFixture.getStartAt());
        reservationTimeService.createReservationTime(reservationTimeRequest.toEntity());
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("모든 예약 시간 조회 테스트")
    @Test
    void findAllReservationTimes() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", reservationTimeFixture.getStartAt());
        List<ReservationTime> reservationTimes = reservationTimeService.findAllReservationTimes();
        assertThat(reservationTimes).hasSize(1);
    }

    @DisplayName("예약 시간 삭제 테스트")
    @Test
    void deleteReservationTime() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", reservationTimeFixture.getStartAt());
        reservationTimeService.deleteReservationTime(1L);
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);
        assertThat(count).isEqualTo(0);
    }
}
