package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @DisplayName("날짜가 형식에 맞지 않을 때 예외를 던진다.")
    @Test
    void validateDateTest_whenDateFormatIsNotMatch() {
        String date = "20-20-20";
        ReservationTime time = new ReservationTime("09:00");

        assertThatThrownBy(() -> new Reservation("커찬", date, time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜(%s)가 yyyy-MM-dd에 맞지 않습니다.".formatted(date));
    }

    @DisplayName("예약자명 비어있을 때 예외를 던진다.")
    @Test
    void validateReservationTest_whenNameIsNull() {
        String date = "2024-10-20";
        ReservationTime time = new ReservationTime("09:00");

        assertThatThrownBy(() ->
                new Reservation(1L, null, date, time))
                .isInstanceOf(NullPointerException.class)
                .hasMessage( "인자 중 null 값이 존재합니다.");
    }

    @DisplayName("날짜 비어있을 때 예외를 던진다.")
    @Test
    void validateReservationTest_whenDateIsNull() {
        ReservationTime time = new ReservationTime("09:00");

        assertThatThrownBy(() ->
                new Reservation(1L, "커찬", null, time))
                .isInstanceOf(NullPointerException.class)
                .hasMessage( "인자 중 null 값이 존재합니다.");
    }

    @DisplayName("시간 비어있을 때 예외를 던진다.")
    @Test
    void validateReservationTest_whenTimeIsNull() {
        String date = "2024-10-20";

        assertThatThrownBy(() ->
                new Reservation(1L, "커찬", date, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage( "인자 중 null 값이 존재합니다.");
    }

    @DisplayName("날짜를 통해 특정 시간대 이전임을 알 수 있다.")
    @Test
    void isAfterTest_whenDateIsBefore() {
        Reservation reservation = new Reservation(1L, "커찬", "2024-04-30", new ReservationTime(1L, "09:00"));
        LocalDateTime currentDateTime = LocalDateTime.of(2024, 5, 1, 10, 0);

        assertThat(reservation.isAfter(currentDateTime)).isFalse();
    }

    @DisplayName("날짜를 통해 특정 시간대 이후임을 알 수 있다.")
    @Test
    void isAfterTest_whenDateIsAfter() {
        Reservation reservation = new Reservation(1L, "커찬", "2024-04-30", new ReservationTime(1L, "09:00"));
        LocalDateTime currentDateTime = LocalDateTime.of(2024, 4, 29, 10, 0);

        assertThat(reservation.isAfter(currentDateTime)).isTrue();
    }

    @DisplayName("날짜가 같은 경우, 시간을 통해 판단한다.")
    @Test
    void isAfterTest_whenDateIsEqualTimeIsBefore() {
        Reservation reservation = new Reservation(1L, "커찬", "2024-04-30", new ReservationTime(1L, "09:00"));
        LocalDateTime currentDateTime = LocalDateTime.of(2024, 4, 30, 10, 0);

        assertThat(reservation.isAfter(currentDateTime)).isFalse();
    }
}
