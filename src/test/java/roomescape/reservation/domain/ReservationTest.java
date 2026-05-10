package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalTime;

class ReservationTest {

    private final ReservationTime time = ReservationTime.of(1L, LocalTime.of(10, 0), LocalTime.of(11, 0));
    private final Theme theme = Theme.of(1L, "테마1", "설명", "https://image.com");
    private final LocalDate futureDate = LocalDate.now().plusDays(1);

    @Test
    @DisplayName("정상 예약 생성")
    void 정상_예약_생성() {
        assertThatCode(() -> Reservation.of("현미밥", futureDate, time, theme))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이름이 null이면 예외 발생")
    void 이름_null_예외() {
        assertThatThrownBy(() -> Reservation.of(null, futureDate, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 필수입니다.");
    }

    @Test
    @DisplayName("이름이 공백이면 예외 발생")
    void 이름_공백_예외() {
        assertThatThrownBy(() -> Reservation.of("  ", futureDate, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 필수입니다.");
    }

    @Test
    @DisplayName("날짜가 null이면 예외 발생")
    void 날짜_null_예외() {
        assertThatThrownBy(() -> Reservation.of("현미밥", null, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜는 필수입니다.");
    }

    @Test
    @DisplayName("과거 날짜면 예외 발생")
    void 과거_날짜_예외() {
        assertThatThrownBy(() -> Reservation.of("현미밥", LocalDate.now().minusDays(1), time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재보다 이전의 날짜는 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("시간이 null이면 예외 발생")
    void 시간_null_예외() {
        assertThatThrownBy(() -> Reservation.of("현미밥", futureDate, null, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간은 필수입니다.");
    }

    @Test
    @DisplayName("테마가 null이면 예외 발생")
    void 테마_null_예외() {
        assertThatThrownBy(() -> Reservation.of("현미밥", futureDate, time, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마는 필수입니다.");
    }
}