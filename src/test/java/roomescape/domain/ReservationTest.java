package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class ReservationTest {

    @Test
    @DisplayName("올바른 정보로 예약을 생성하면 성공한다")
    void create_reservation_success() {
        // given
        String name = "아나키";
        LocalDate date = LocalDate.of(2026, 5, 4);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포", "너무무서워", "/horror");

        // when
        Reservation reservation = new Reservation(name, date, time, theme);

        // then
        assertThat(reservation.getName()).isEqualTo(name);
        assertThat(reservation.getDate()).isEqualTo(date);
        assertThat(reservation.getTime()).isEqualTo(time);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("예약자 이름이 null이면 예외가 발생한다")
    void validate_name_fail(String name) {
        // given
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포", "너무무서워", "/horror");

        // when & then
        assertThatThrownBy(() -> new Reservation(name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자명이 유효하지 않습니다.");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("예약 날짜가 null이면 예외가 발생한다")
    void validate_date_fail(LocalDate date) {
        // given
        String name = "아나키";
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포", "너무무서워", "/horror");

        // when & then
        assertThatThrownBy(() -> new Reservation(name, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜가 유효하지 않습니다.");
    }
}
