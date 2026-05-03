package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.time.domain.ReservationTime;

class ReservationTest {

    private final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

    @Test
    @DisplayName("성공적으로 예약 도메인 객체를 생성한다.")
    void createSuccess() {
        // given
        String name = "브라운";
        String date = "2024-05-01";

        // when
        Reservation reservation = Reservation.create(name, date, reservationTime);

        // then
        assertThat(reservation.getName()).isEqualTo(name);
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2024, 5, 1));
        assertThat(reservation.getTime()).isEqualTo(reservationTime);
    }

    @Test
    @DisplayName("이름이 10글자를 초과하면 예외가 발생한다.")
    void validateNameSizeTest() {
        // given
        String longName = "열한글자짜리이름입니다아";

        // when & then
        assertThatThrownBy(() -> Reservation.create(longName, "2024-05-01", reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름은 10글자 이하여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024/05/01", "24-05-01", "not-a-date", "2024-13-01", "2024-02-30"})
    @DisplayName("잘못된 날짜 형식이나 존재하지 않는 날짜 입력 시 예외가 발생한다.")
    void validateDateFormatTest(String invalidDate) {
        // when & then
        assertThatThrownBy(() -> Reservation.create("브라운", invalidDate, reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("날짜 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("생성된 예약 객체의 필드 값을 확인한다.")
    void getterTest() {
        // given
        LocalDate date = LocalDate.of(2024, 5, 1);
        Reservation reservation = new Reservation(1L, "제임스", date, reservationTime);

        // then
        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getName()).isEqualTo("제임스");
        assertThat(reservation.getDate()).isEqualTo(date);
        assertThat(reservation.getTime()).isEqualTo(reservationTime);
    }
}
