package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.config.FixedClockConfig.TODAY;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("올바른 정보로 예약을 생성하면 성공한다.")
    void 정상_예약_테스트() {
        String name = "아나키";
        LocalDate date = LocalDate.parse(TODAY);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포", "너무무서워", "/horror");

        assertDoesNotThrow(() -> new Reservation(name, date, time, theme));
    }

    @Test
    @DisplayName("예약자 이름이 null 이면 예외가 발생한다.")
    void 이름이_null_예외_테스트() {
        String name = null;
        LocalDate date = LocalDate.parse(TODAY);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포", "너무무서워", "/horror");

        assertThatThrownBy(() -> new Reservation(name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 비어 있을 수 없습니다.");
    }

    @Test
    @DisplayName("예약자 이름이 빈칸이면 예외가 발생한다.")
    void 이름이_빈칸_예외_테스트() {
        String name = "";
        LocalDate date = LocalDate.parse(TODAY);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포", "너무무서워", "/horror");

        assertThatThrownBy(() -> new Reservation(name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 비어 있을 수 없습니다.");
    }

    @Test
    @DisplayName("예약 날짜가 null 이면 예외가 발생한다.")
    void 날짜가_null_예외_테스트() {
        String name = "아나키";
        LocalDate date = null;
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포", "너무무서워", "/horror");

        assertThatThrownBy(() -> new Reservation(name, date, time, theme))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("예약 날짜가 비어 있습니다.");
    }

    @Test
    @DisplayName("예약 시간이 null 이면 예외가 발생한다.")
    void 시간이_null_예외_테스트() {
        String name = "아나키";
        LocalDate date = LocalDate.parse(TODAY);
        ReservationTime time = null;
        Theme theme = new Theme(1L, "공포", "너무무서워", "/horror");

        assertThatThrownBy(() -> new Reservation(name, date, time, theme))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("시간이 비어 있습니다.");
    }

    @Test
    @DisplayName("테마가 null 이면 예외가 발생한다.")
    void 테마가_null_예외_테스트() {
        String name = "아나키";
        LocalDate date = LocalDate.parse(TODAY);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = null;

        assertThatThrownBy(() -> new Reservation(name, date, time, theme))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("테마가 비어 있습니다.");
    }
}
