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
    void equalIdTest(final long firstId, final long secondId, boolean result) {

        // given
        Reservation reservation = new Reservation(firstId, "체체", LocalDate.of(2024, 12, 12),
                new ReservationTime(1L, LocalTime.of(10, 0)));

        // when & then
        assertThat(reservation.isEqualId(secondId)).isEqualTo(result);
    }

    @DisplayName("이름은 255자 초과면 예외를 발생한다.")
    @Test
    void inValidateNameLengthThrowExceptionTest() {
        // given
        String nameOver255 = "a".repeat(256);
        LocalDate date = LocalDate.of(2024, 12, 12);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

        // when & then
        assertThatThrownBy(() -> new Reservation(nameOver255, date, time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 255자 이하로 입력해야 합니다.");
    }

}
