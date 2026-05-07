package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.TimeSlot;
import roomescape.repository.FakeReservationDao;
import roomescape.repository.FakeTimeDao;

class TimeSlotServiceTest {

    private TimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        FakeTimeDao fakeTimeDao = new FakeTimeDao();
        FakeReservationDao fakeReservationDao = new FakeReservationDao();
        reservationTimeService = new TimeService(fakeTimeDao, fakeReservationDao);
    }

    @Test
    @DisplayName("시간 정보를 입력하여 새로운 예약 시간을 생성하고 반환한다.")
    void saveTime() {
        TimeSlot timeSlot = reservationTimeService.saveTime(LocalTime.of(10, 0));
        assertThat(timeSlot.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("존재하는 예약 시간을 삭제하면 전체 목록에서 사라진다.")
    void removeTime() {
        TimeSlot timeSlot = reservationTimeService.saveTime(LocalTime.of(10, 0));
        reservationTimeService.removeTime(timeSlot.id());
        assertThat(reservationTimeService.allTimes()).isEmpty();
    }

    @Test
    @DisplayName("모든 예약 시간 목록을 조회하여 반환한다.")
    void allTimes() {
        reservationTimeService.saveTime(LocalTime.of(10, 0));
        List<TimeSlot> timeSlots = reservationTimeService.allTimes();
        assertThat(timeSlots).hasSize(1);
    }

    @Test
    @DisplayName("식별자를 통해 특정 예약 시간 객체를 조회한다.")
    void findTime() {
        TimeSlot savedTimeSlot = reservationTimeService.saveTime(LocalTime.of(10, 0));
        TimeSlot foundTimeSlot = reservationTimeService.findTime(savedTimeSlot.id());
        assertThat(foundTimeSlot.startAt()).isEqualTo(LocalTime.of(10, 0));
    }
}
