package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidClientRequestException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @DisplayName("이름에 공백이나 빈 값일 경우 예외가 발생한다.")
    @NullSource
    @ValueSource(strings = {" ", "\n", ""})
    @ParameterizedTest
    void given_when_newWithEmptyName_then_throwException(String invalidName) {
        //given, when, then
        assertThatThrownBy(() -> new Reservation(invalidName, LocalDate.parse("2099-01-01"),
                new TimeSlot(1L, "10:00"), new Theme(1L, "name", "description", "thumbnail"))).isInstanceOf(InvalidClientRequestException.class);
    }
}