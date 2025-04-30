package roomescape.model;

import java.time.LocalDateTime;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("이름에 null값이 들어오면 예외처리되도록 한다.")
    @Test
    void test1() {
        // given
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);

        // when & then
        assertThatThrownBy(() ->
                new Reservation(
                        1L,
                        null,
                        dateTime.toLocalDate(),
                        new ReservationTime(dateTime.toLocalTime()),
                        new Theme(1L, "공포", "무서워요", "image"))
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름에 공백값 들어오면 예외처리되도록 한다.")
    @Test
    void test2() {
        // given
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);

        // when & then
        assertThatThrownBy(() ->
                new Reservation(
                        1L,
                        "",
                        dateTime.toLocalDate(),
                        new ReservationTime(dateTime.toLocalTime()),
                        new Theme(1L, "공포", "무서워요", "image"))
        )
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("당일 예약은 예외처리되도록 한다.")
    @Test
    void test3() {
        // given
        LocalDateTime dateTime = LocalDateTime.now();
        Reservation reservation = new Reservation(
                1L,
                "히로",
                dateTime.toLocalDate(),
                new ReservationTime(dateTime.toLocalTime()),
                new Theme(1L, "공포", "무서워요", "image"));

        // when & then
        assertThatThrownBy(reservation::validateReservationDateInFuture
        )
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("오늘보다 과거로 예약하려고 할 경우 예외처리되도록 한다.")
    @Test
    void test4() {
        // given
        LocalDateTime dateTime = LocalDateTime.now().minusDays(1);
        Reservation reservation = new Reservation(
                1L,
                "히로",
                dateTime.toLocalDate(),
                new ReservationTime(dateTime.toLocalTime()),
                new Theme(1L, "공포", "무서워요", "image"));

        // when & then
        assertThatThrownBy(reservation::validateReservationDateInFuture
        )
                .isInstanceOf(IllegalStateException.class);
    }
}
