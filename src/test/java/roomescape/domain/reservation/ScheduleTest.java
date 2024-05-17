package roomescape.domain.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ScheduleTest {
    @DisplayName("일정이 현재보다 이전이다.")
    @Test
    void isBeforeNow() {
        //given
        ReservationDate date = new ReservationDate(LocalDate.now().minusDays(1).toString());
        ReservationTime time = new ReservationTime(LocalTime.now().toString());
        Schedule schedule = new Schedule(date, time);

        //when&then
        assertThat(schedule.isBeforeNow()).isTrue();
    }

    @DisplayName("일정이 현재보다 이후이다.")
    @Test
    void isAfterNow() {
        //given
        ReservationDate date = new ReservationDate(LocalDate.now().plusDays(1).toString());
        ReservationTime time = new ReservationTime(LocalTime.now().toString());
        Schedule schedule = new Schedule(date, time);

        //when&then
        assertThat(schedule.isBeforeNow()).isFalse();
    }
}
