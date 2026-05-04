package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.repository.FakeReservationTimeDao;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao();
        reservationTimeService = new ReservationTimeService(fakeReservationTimeDao);
    }

    @Test
    @DisplayName("시간 정보를 입력하여 새로운 예약 시간을 생성하고 반환한다.")
    void saveTime() {
        ReservationTime reservationTime = reservationTimeService.saveTime(LocalTime.of(10, 0));
        assertThat(reservationTime.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("존재하는 예약 시간을 삭제하면 전체 목록에서 사라진다.")
    void removeTime() {
        ReservationTime reservationTime = reservationTimeService.saveTime(LocalTime.of(10, 0));
        reservationTimeService.removeTime(reservationTime.id());
        assertThat(reservationTimeService.allTimes()).isEmpty();
    }

    @Test
    @DisplayName("모든 예약 시간 목록을 조회하여 반환한다.")
    void allTimes() {
        reservationTimeService.saveTime(LocalTime.of(10, 0));
        List<ReservationTime> times = reservationTimeService.allTimes();
        assertThat(times).hasSize(1);
    }

    @Test
    @DisplayName("식별자를 통해 특정 예약 시간 객체를 조회한다.")
    void findTime() {
        ReservationTime savedTime = reservationTimeService.saveTime(LocalTime.of(10, 0));
        ReservationTime foundTime = reservationTimeService.findTime(savedTime.id());
        assertThat(foundTime.startAt()).isEqualTo(LocalTime.of(10, 0));
    }
}
