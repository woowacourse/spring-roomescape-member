package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.config.FixedClockConfig.TODAY;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.theme.ThemeName;
import roomescape.domain.reservation.time.ReservationTime;
import roomescape.domain.reservation.theme.Theme;

class ReservationTest {

    @Test
    @DisplayName("올바른 정보로 예약을 생성하면 성공한다.")
    void 정상_예약_테스트() {
        UserName userName = UserName.parse("아나키");
        LocalDate date = LocalDate.parse(TODAY);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, ThemeName.parse("공포"), "너무무서워", "/horror");

        assertDoesNotThrow(() -> new Reservation(userName, date, time, theme));
    }

    @Test
    @DisplayName("예약자 이름이 null 이면 예외가 발생한다.")
    void 이름이_null_예외_테스트() {
        UserName userName = null;
        LocalDate date = LocalDate.parse(TODAY);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, ThemeName.parse("공포"), "너무무서워", "/horror");

        assertThatThrownBy(() -> new Reservation(userName, date, time, theme))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("예약자 이름이 비어 있습니다.");
    }

    @Test
    @DisplayName("예약 날짜가 null 이면 예외가 발생한다.")
    void 날짜가_null_예외_테스트() {
        UserName userName = UserName.parse("아나키");
        LocalDate date = null;
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, ThemeName.parse("공포"), "너무무서워", "/horror");

        assertThatThrownBy(() -> new Reservation(userName, date, time, theme))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("예약 날짜가 비어 있습니다.");
    }

    @Test
    @DisplayName("예약 시간이 null 이면 예외가 발생한다.")
    void 시간이_null_예외_테스트() {
        UserName userName = UserName.parse("아나키");
        LocalDate date = LocalDate.parse(TODAY);
        ReservationTime time = null;
        Theme theme = new Theme(1L, ThemeName.parse("공포"), "너무무서워", "/horror");

        assertThatThrownBy(() -> new Reservation(userName, date, time, theme))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("시간이 비어 있습니다.");
    }

    @Test
    @DisplayName("테마가 null 이면 예외가 발생한다.")
    void 테마가_null_예외_테스트() {
        UserName userName = UserName.parse("아나키");
        LocalDate date = LocalDate.parse(TODAY);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = null;

        assertThatThrownBy(() -> new Reservation(userName, date, time, theme))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("테마가 비어 있습니다.");
    }
}
