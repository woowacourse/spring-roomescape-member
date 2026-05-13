package roomescape.reservation.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservation.domain.exception.IllegalReservationDateTimeException;
import roomescape.reservation.domain.exception.IllegalStateReservationException;
import roomescape.reservation.domain.exception.UnauthorizedReservationChangeException;
import roomescape.time.domain.ReservationTime;

@SpringBootTest
class ReservationTest {

    @Autowired
    private Clock clock;

    @Test
    @DisplayName("이름이 동일하고, 취소된 예약이 아니라면, 과거 시간이 아닌 경우 통과한다.")
    void normalTest() {
        ReservationTime time = ReservationTime.builder()
                .startAt(LocalTime.now(clock))
                .build();
        Reservation reservation = Reservation.builder()
                .name("포비")
                .date(LocalDate.now(clock))
                .time(time)
                .build();
        Assertions.assertThatCode(() -> reservation.checkChangeable("포비", clock))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("다른 이름이면 예약 변경 권한이 없어서 예외를 반환한다.")
    void unauthorizedTest() {
        ReservationTime time = ReservationTime.builder()
                .startAt(LocalTime.now(clock))
                .build();
        Reservation reservation = Reservation.builder()
                .name("포비")
                .date(LocalDate.now(clock))
                .time(time)
                .build();
        Assertions.assertThatThrownBy(() -> reservation.checkChangeable("리사", clock))
                .isInstanceOf(UnauthorizedReservationChangeException.class);
    }

    @Test
    @DisplayName("취소된 예약을 변경하려고 하면 예외를 반환한다.")
    void canceledTest() {
        ReservationTime time = ReservationTime.builder()
                .startAt(LocalTime.now(clock))
                .build();
        Reservation reservation = Reservation.builder()
                .name("포비")
                .date(LocalDate.now(clock))
                .time(time)
                .status(Status.CANCELED)
                .build();
        Assertions.assertThatThrownBy(() -> reservation.checkChangeable("포비", clock))
                .isInstanceOf(IllegalStateReservationException.class);
    }

    @Test
    @DisplayName("이미 지난 예약을 변경하려고 하면 예외를 반환한다.")
    void timeOverTest() {
        ReservationTime time = ReservationTime.builder()
                .startAt(LocalTime.now(clock))
                .build();
        Reservation reservation = Reservation.builder()
                .name("포비")
                .date(LocalDate.now(clock).minusDays(1))
                .time(time)
                .build();
        Assertions.assertThatThrownBy(() -> reservation.checkChangeable("포비", clock))
                .isInstanceOf(IllegalReservationDateTimeException.class);
    }
}
