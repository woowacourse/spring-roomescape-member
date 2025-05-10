package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.custom.InvalidInputException;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.RoomTheme;

class ReservationTest {
    private static final String NAME = "검프";
    private static final LocalDate DATE = LocalDate.now().plusDays(1);
    private static final ReservationTime RESERVATION_TIME = new ReservationTime(1L, LocalTime.of(10, 0));
    private static final RoomTheme ROOM_THEME = new RoomTheme(1L, "우테코", "미션 2는 방탈출 사용자 예약", "url");

    @DisplayName("예약 생성시, 예약자 명이 빈 값이면 예외를 던진다")
    @Test
    void createReservationTest_WhenNameIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new Reservation(null, DATE, RESERVATION_TIME, ROOM_THEME))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약자 명은 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("예약 생성시, 예약 날짜가 빈 값이면 예외를 던진다")
    @Test
    void createReservationTest_WhenDateIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new Reservation(NAME, null, RESERVATION_TIME, ROOM_THEME))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약 날짜는 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("예약 생성시, 예약 시간이 빈 값이면 예외를 던진다")
    @Test
    void createReservationTest_WhenReservationTimeIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new Reservation(NAME, DATE, null, ROOM_THEME))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약 시간은 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("예약 생성시, 예약 테마가 빈 값이면 예외를 던진다")
    @Test
    void createReservationTest_WhenRoomThemeIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new Reservation(NAME, DATE, RESERVATION_TIME, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약 테마는 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("해당 예약의 날짜가 과거 시점이면 true를 반환한다")
    @Test
    void isPastDateAndTimeTest_WhenDateIsPast() {
        // given
        final LocalDate pastDate = LocalDate.now().minusDays(1);
        final Reservation reservation = new Reservation(NAME, pastDate, RESERVATION_TIME, ROOM_THEME);

        // when // then
        assertThat(reservation.isPastDateAndTime()).isTrue();
    }

    @DisplayName("해당 예약의 날짜가 미래 시점이면 false를 반환한다")
    @Test
    void isPastDateAndTimeTest_WhenDateIsFuture() {
        // given
        final Reservation reservation = new Reservation(NAME, DATE, RESERVATION_TIME, ROOM_THEME);

        // when // then
        assertThat(reservation.isPastDateAndTime()).isFalse();
    }

    @DisplayName("해당 예약의 날짜가 오늘 날짜이고, 시간이 과거 시간대 이면 true를 반환한다")
    @Test
    void isPastDateAndTimeTest_WhenDateIsTodayAndTimeIsPast() {
        // given
        final ReservationTime pastTime = new ReservationTime(2L, LocalTime.now().minusHours(1));
        final Reservation reservation = new Reservation(NAME, LocalDate.now(), pastTime, ROOM_THEME);

        // when // then
        assertThat(reservation.isPastDateAndTime()).isTrue();
    }

    @DisplayName("해당 예약의 날짜가 오늘 날짜이고, 시간이 미래 시간대 이면 false를 반환한다")
    @Test
    void isPastDateAndTimeTest_WhenDateIsTodayAndTimeIsFuture() {
        // given
        final ReservationTime futureTime = new ReservationTime(2L, LocalTime.now().plusMinutes(1));
        final Reservation reservation = new Reservation(NAME, LocalDate.now(), futureTime, ROOM_THEME);

        // when // then
        assertThat(reservation.isPastDateAndTime()).isFalse();
    }
}
