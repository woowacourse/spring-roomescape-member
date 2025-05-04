package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReservationTest {

    @DisplayName("아이디가 같으면 true를, 다르면 false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 1, true",
            "1, 2, false"})
    void equalIdTest(final long firstId, final long secondId, final boolean result) {

        // given
        final Reservation reservation1 = Reservation.load(firstId, "체체", LocalDate.now(),
                new ReservationTime(1L, LocalTime.now().plusHours(1)),
                new Theme(1L, "test", "test", "test"));
        final Reservation reservation2 = Reservation.load(secondId, "체체", LocalDate.now(),
                new ReservationTime(1L, LocalTime.now().plusHours(1)),
                new Theme(1L, "test", "test", "test"));

        // when & then
        assertThat(reservation1.equals(reservation2)).isEqualTo(result);
    }

    @DisplayName("이름은 10자 초과면 예외를 발생한다.")
    @Test
    void inValidateNameLengthThrowExceptionTest() {
        // given
        final String nameOver10 = "a".repeat(11);
        final LocalDate date = LocalDate.now();
        final ReservationTime time = new ReservationTime(LocalTime.now().plusHours(1));
        final Theme theme = new Theme(1L, "test", "test", "test");

        // when & then
        assertThatThrownBy(() -> Reservation.create(nameOver10, date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자명은 10자 이하여야합니다.");
    }

    @DisplayName("날짜가 과거인 경우 예외를 발생한다.")
    @Test
    void createReservationThrowExceptionIfDateIsPast() {

        // given
        final LocalDate date = LocalDate.of(2024, 12, 12);
        final ReservationTime time = new ReservationTime(LocalTime.now().plusHours(1));
        final Theme theme = new Theme(1L, "test", "test", "test");

        // when & then
        assertThatThrownBy(() -> Reservation.create("test", date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약은 미래만 가능합니다.");
    }

    @DisplayName("날짜가 당일이고 시간이 과거인 경우 예외를 발생한다.")
    @Test
    void createReservationThrowExceptionIfDateIsTodayAndTimeIsPast() {

        // given
        final LocalDate date = LocalDate.now();
        final ReservationTime time = new ReservationTime(LocalTime.now().minusHours(1));
        final Theme theme = new Theme(1L, "test", "test", "test");

        // when & then
        assertThatThrownBy(() -> Reservation.create("test", date, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약은 미래만 가능합니다.");
    }
}
