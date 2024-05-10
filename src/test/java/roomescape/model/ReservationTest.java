package roomescape.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("비어 있는 날짜 입력 시 예외 발생")
    @Test
    void emptyDate() {
        assertThatThrownBy(() ->
                new Reservation(null, null, new ReservationTime(LocalTime.parse("10:00")), new Theme("이름", "설명", "썸네일")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜가 비어 있습니다.");
    }

    @Test
    @DisplayName("예약 날짜와 시간이 현재보다 이전인 경우 isBeforeNow()가 참을 반환")
    void isBeforeNowWithPastDate() {
        final Theme theme = new Theme("테마 이름", "테마 설명", "테마 썸네일");
        final ReservationTime time = new ReservationTime(LocalTime.of(14, 0));
        final Reservation reservation = new Reservation(null, LocalDate.now().minusDays(1), time, theme);
        assertTrue(reservation.isBefore(LocalDateTime.now()));
    }

    @Test
    @DisplayName("예약 날짜와 시간이 현재보다 이후인 경우 isBeforeNow()가 거짓을 반환")
    void isBeforeNowWithFutureDate() {
        final Theme theme = new Theme("테마 이름", "테마 설명", "테마 썸네일");
        final ReservationTime time = new ReservationTime(LocalTime.of(14, 0));
        final Reservation reservation = new Reservation(null, LocalDate.now().plusDays(1), time, theme);
        assertFalse(reservation.isBefore(LocalDateTime.now()));
    }
}
