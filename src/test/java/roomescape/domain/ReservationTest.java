package roomescape.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    void 예약자명은_2글자_이상_5글자_이하만_가능하다_성공() {
        assertThatCode(() -> new Reservation(1L, "가나다", LocalDate.now(), new ReservationTime(LocalTime.of(12, 0))))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"가", "가나다라마바"})
    void 예약자명은_2글자_이상_5글자_이하만_가능하다_실패(String name) {
        assertThatThrownBy(() -> new Reservation(1L, name, LocalDate.now(), new ReservationTime(LocalTime.of(12, 0))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("예약자명은 2글자에서 5글자까지만 가능합니다.");
    }

    @ParameterizedTest
    @CsvSource({"2025-04-23T12:30, 2025-04-22T12:30", "2025-04-23T12:30, 2025-04-23T12:00"})
    void 지난_날짜에_대한_예약이라면_예외가_발생한다(LocalDateTime currentDateTime, LocalDateTime reservationDateTime) {
        assertThatThrownBy(() -> new Reservation("test", currentDateTime, reservationDateTime.toLocalDate(), new ReservationTime(reservationDateTime.toLocalTime())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 날짜와 시간에 대한 예약은 불가능합니다.");
    }

    @ParameterizedTest
    @CsvSource({"2025-04-23T12:30, 2025-04-23T12:30", "2025-04-23T12:30, 2025-04-23T12:39"})
    void 예약일이_오늘인_경우_예약_시간까지_10분도_남지_않았다면_예외가_발생한다(LocalDateTime currentDateTime, LocalDateTime reservationDateTime) {
        assertThatThrownBy(() -> new Reservation("test", currentDateTime, reservationDateTime.toLocalDate(), new ReservationTime(reservationDateTime.toLocalTime())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간까지 10분도 남지 않아 예약이 불가합니다.");
    }
}