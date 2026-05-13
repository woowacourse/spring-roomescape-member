package roomescape.time.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservation.domain.exception.IllegalReservationDateTimeException;

@SpringBootTest
class ReservationTimeTest {

    @Autowired
    private Clock clock;

    @Test
    @DisplayName("미래 시간이면 통과한다.")
    void normalTest() {
        ReservationTime time = ReservationTime.builder()
                .startAt(LocalTime.now(clock))
                .build();
        Assertions.assertThatCode(() -> time.checkValidDateTime(LocalDate.now(clock), clock))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("과거 시간이면 에러를 반환한다.")
    void invalidDateTest() {
        ReservationTime time = ReservationTime.builder()
                .startAt(LocalTime.now(clock))
                .build();
        Assertions.assertThatThrownBy(() -> time.checkValidDateTime(LocalDate.now(clock).minusDays(1), clock))
                .isInstanceOf(IllegalReservationDateTimeException.class);
    }

    @Test
    @DisplayName("오늘 날짜여도, 시간이 지나면 에러를 반환한다.")
    void invalidTimeTest() {
        ReservationTime time = ReservationTime.builder()
                .startAt(LocalTime.now(clock).minusHours(1))
                .build();
        Assertions.assertThatThrownBy(() -> time.checkValidDateTime(LocalDate.now(clock), clock))
                .isInstanceOf(IllegalReservationDateTimeException.class);
    }
}
