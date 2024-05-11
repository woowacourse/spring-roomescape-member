package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.util.Fixture.DATE;
import static roomescape.util.Fixture.ID;
import static roomescape.util.Fixture.MEMBER;
import static roomescape.util.Fixture.RESERVATION_TIME;
import static roomescape.util.Fixture.THEME;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.BadRequestException;

class ReservationTest {
    @Test
    @DisplayName("예약을 생성한다")
    void createReservation() {
        assertThatCode(() -> new Reservation(ID, MEMBER, DATE, RESERVATION_TIME, THEME))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예약 생성 시, member가 null이면 예외가 발생한다")
    void throwExceptionWhenEmptyMember() {
        assertThatThrownBy(() -> new Reservation(ID, null, DATE, RESERVATION_TIME, THEME))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("예약자는 null일 수 없습니다.");
    }

    @Test
    @DisplayName("예약 생성 시, date가 null이면 예외가 발생한다")
    void throwExceptionWhenDateNull() {
        final LocalDate nullDate = null;
        assertThatThrownBy(() -> new Reservation(ID, MEMBER, nullDate, RESERVATION_TIME, THEME))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("예약 날짜는 null일 수 없습니다.");
    }

    @Test
    @DisplayName("예약 생성 시, date가 유효한 날짜 형식이 아니면 예외가 발생한다")
    void throwExceptionWhenInvalidDateFormat() {
        assertThatThrownBy(() -> new Reservation(ID, MEMBER, "2024/5/5", RESERVATION_TIME, THEME))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("예약 날짜 형식이 잘못되었습니다.");
    }

    @Test
    @DisplayName("예약 생성 시, reservationTime이 null이 들어오면 예외가 발생한다.")
    void throwExceptionWhenNullReservationTime() {
        assertThatThrownBy(() -> new Reservation(ID, MEMBER, DATE, null, THEME))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("예약 시간은 null일 수 없습니다.");
    }

    @Test
    @DisplayName("예약 생성 시, theme가 null이 들어오면 예외가 발생한다.")
    void throwExceptionWhenNullTheme() {
        assertThatThrownBy(() -> new Reservation(ID, MEMBER, DATE, RESERVATION_TIME, null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("예약 테마는 null일 수 없습니다.");
    }

    @Test
    @DisplayName("예약 날짜가 과거인지 여부를 반환한다.")
    void isDatePast() {
        final LocalDate today = LocalDate.now();
        final LocalDate yesterday = today.minusDays(1);
        final LocalDate tomorrow = today.plusDays(1);

        final Reservation yesterdayReservation = new Reservation(ID, MEMBER, yesterday, RESERVATION_TIME, THEME);
        final Reservation todayReservation = new Reservation(ID, MEMBER, today, RESERVATION_TIME, THEME);
        final Reservation tomorrowReservation = new Reservation(ID, MEMBER, tomorrow, RESERVATION_TIME, THEME);

        assertAll(
                () -> assertThat(yesterdayReservation.isDatePast()).isTrue(),
                () -> assertThat(todayReservation.isDatePast()).isFalse(),
                () -> assertThat(tomorrowReservation.isDatePast()).isFalse()
        );
    }

    @Test
    @DisplayName("예약 날짜가 오늘인지 여부를 반환한다.")
    void isDateToday() {
        final LocalDate today = LocalDate.now();
        final LocalDate yesterday = today.minusDays(1);
        final LocalDate tomorrow = today.plusDays(1);

        final Reservation yesterdayReservation = new Reservation(ID, MEMBER, yesterday, RESERVATION_TIME, THEME);
        final Reservation todayReservation = new Reservation(ID, MEMBER, today, RESERVATION_TIME, THEME);
        final Reservation tomorrowReservation = new Reservation(ID, MEMBER, tomorrow, RESERVATION_TIME, THEME);

        assertAll(
                () -> assertThat(yesterdayReservation.isDateToday()).isFalse(),
                () -> assertThat(todayReservation.isDateToday()).isTrue(),
                () -> assertThat(tomorrowReservation.isDateToday()).isFalse()
        );
    }

    @Test
    @DisplayName("예약 날짜를 yyyy-MM-dd 형식으로 반환한다.")
    void getDateString() {
        final LocalDate date = LocalDate.of(2024, 5, 5);
        final Reservation reservation = new Reservation(ID, MEMBER, date, RESERVATION_TIME, THEME);

        assertThat(reservation.getDateString()).isEqualTo("2024-05-05");
    }
}
