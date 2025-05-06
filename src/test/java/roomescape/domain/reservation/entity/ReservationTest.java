package roomescape.domain.reservation.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.common.exception.InvalidArgumentException;

class ReservationTest {

    @DisplayName("아이디 존재 여부")
    @ParameterizedTest
    @CsvSource(value = {"1,true", "null,false"}, delimiter = ',', nullValues = "null")
    void test1(final Long id, final boolean expected) {
        // given
        final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        final Theme theme = new Theme(1L, "공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        final Reservation reservation = new Reservation(id, "꾹이", LocalDate.now(), reservationTime, theme);

        // when
        final boolean result = reservation.existId();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("25자 이하의 이름을 사용할 수 있다.")
    @Test
    void test2() {
        final String nameLength25 = "aaaaaaaaaabbbbbbbbbbccc25";
        final Theme theme = new Theme(1L, "공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        assertThatCode(
                () -> new Reservation(1L, nameLength25, LocalDate.now(), new ReservationTime(1L, LocalTime.now()),
                        theme)).doesNotThrowAnyException();
    }

    @DisplayName("잘못된 이름을 사용하면 예외를 반환한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "aaaaaaaaaabbbbbbbbbbcccc26"})
    @ParameterizedTest
    void test3(final String name) {
        final Theme theme = new Theme(1L, "공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when & then
        assertThatThrownBy(() -> new Reservation(1L, name, LocalDate.now(), new ReservationTime(1L, LocalTime.now()),
                theme)).isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("예약 날짜는 현재보다 미래여야 한다.")
    @Test
    void test4() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime future = now.plusDays(1);

        final Theme theme = new Theme(1L, "공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final ReservationTime reservationTime = new ReservationTime(1L, future.toLocalTime());
        final Reservation reservation = new Reservation(1L, "예약", future.toLocalDate(), reservationTime, theme);

        // when & then
        assertThatCode(() -> reservation.validateNotPastReservation(now)).doesNotThrowAnyException();
    }

    @DisplayName("예약 날짜가 과거라면 예외를 반환한다.")
    @Test
    void notPastReservation_throwsException() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime pastDay = now.minusDays(1);

        final Theme theme = new Theme(1L, "공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final ReservationTime reservationTime = new ReservationTime(1L, pastDay.toLocalTime());
        final Reservation reservation = new Reservation(1L, "예약", pastDay.toLocalDate(), reservationTime, theme);

        // when & then
        assertThatThrownBy(() -> reservation.validateNotPastReservation(now)).isInstanceOf(
                InvalidArgumentException.class);
    }
}
