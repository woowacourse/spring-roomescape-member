package roomescape.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

//    @ParameterizedTest
//    @ValueSource(strings = {"", " ", "12345678912"})
//    void 유효하지_않은_이름_예외처리(String name) {
//        assertThatThrownBy(() -> new Reservation(null, new UserName(name), null, null, null))
//                .isInstanceOf(IllegalStateException.class);
//    }

    @ParameterizedTest
    @CsvSource(value = {"2024-04-14, true", "2024-04-15, true", "2024-04-16, false"})
    void 예약이_현재보다_과거임을_확인(LocalDate reservationDate, Boolean expected) {
        LocalDate now = LocalDate.of(2024, 4, 15);
        Reservation reservation = new Reservation(null, reservationDate, null, null, null);
        assertThat(reservation.isPast(now)).isEqualTo(expected);
    }
}
