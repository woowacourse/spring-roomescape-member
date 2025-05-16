package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.custom.InvalidInputException;
import roomescape.member.domain.Member;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

class ReservationTest {

    private static final LocalDate DATE = LocalDate.now().plusDays(1);
    private static final ReservationTime RESERVATION_TIME = new ReservationTime(1L, LocalTime.of(10, 0));
    private static final Theme THEME = new Theme(1L, "우테코", "미션 2는 방탈출 사용자 예약", "url");
    private static final Member MEMBER = Member.of(1L, "USER", "라젤", "razel@woowa.com", "password");

    @DisplayName("예약 생성시, 멤버가 빈 값이면 예외를 던진다")
    @Test
    void createReservationTest_WhenNameIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new Reservation(DATE, RESERVATION_TIME, THEME, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("멤버는 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("예약 생성시, 예약 날짜가 빈 값이면 예외를 던진다")
    @Test
    void createReservationTest_WhenDateIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new Reservation(null, RESERVATION_TIME, THEME, MEMBER))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약 날짜는 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("예약 생성시, 예약 시간이 빈 값이면 예외를 던진다")
    @Test
    void createReservationTest_WhenReservationTimeIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new Reservation(DATE, null, THEME, MEMBER))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약 시간은 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("예약 생성시, 예약 테마가 빈 값이면 예외를 던진다")
    @Test
    void createReservationTest_WhenThemeIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new Reservation(DATE, RESERVATION_TIME, null, MEMBER))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약 테마는 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("해당 예약의 날짜가 과거 시점이면 true를 반환한다")
    @Test
    void isPastDateAndTimeTest_WhenDateIsPast() {
        // given
        final LocalDate pastDate = LocalDate.now().minusDays(1);
        final Reservation reservation = new Reservation(pastDate, RESERVATION_TIME, THEME, MEMBER);

        // when // then
        assertThat(reservation.isPastDateAndTime()).isTrue();
    }

    @DisplayName("해당 예약의 날짜가 미래 시점이면 false를 반환한다")
    @Test
    void isPastDateAndTimeTest_WhenDateIsFuture() {
        // given
        final Reservation reservation = new Reservation(DATE, RESERVATION_TIME, THEME, MEMBER);

        // when // then
        assertThat(reservation.isPastDateAndTime()).isFalse();
    }

    @DisplayName("해당 예약의 날짜가 오늘 날짜이고, 시간이 과거 시간대 이면 true를 반환한다")
    @Test
    void isPastDateAndTimeTest_WhenDateIsTodayAndTimeIsPast() {
        // given
        final ReservationTime pastTime = new ReservationTime(2L, LocalTime.now().minusHours(1));
        final Reservation reservation = new Reservation(LocalDate.now(), pastTime, THEME, MEMBER);

        // when // then
        assertThat(reservation.isPastDateAndTime()).isTrue();
    }

    @DisplayName("해당 예약의 날짜가 오늘 날짜이고, 시간이 미래 시간대 이면 false를 반환한다")
    @Test
    void isPastDateAndTimeTest_WhenDateIsTodayAndTimeIsFuture() {
        // given
        final ReservationTime futureTime = new ReservationTime(2L, LocalTime.now().plusMinutes(1));
        final Reservation reservation = new Reservation(LocalDate.now(), futureTime, THEME, MEMBER);

        // when // then
        assertThat(reservation.isPastDateAndTime()).isFalse();
    }
}
