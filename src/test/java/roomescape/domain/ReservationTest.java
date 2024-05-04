package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.BadRequestException;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.TestFixture.*;

class ReservationTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"123", "", " "})
    @DisplayName("예약자 이름은 빈칸이거나 숫자로만 구성될 수 없다.")
    void validateName(String invalidName) {
        // when & then
        assertThatThrownBy(() -> new Reservation(invalidName, MIA_RESERVATION_DATE, new ReservationTime(MIA_RESERVATION_TIME), WOOTECO_THEME()))
                .isInstanceOf(BadRequestException.class);
    }

    @ParameterizedTest
    @MethodSource("invalidLocalDate")
    @DisplayName("예약 날짜는 현재 날짜 이후이다.")
    void validateDate(LocalDate invalidDate) {
        // when & then
        assertThatThrownBy(() -> new Reservation(USER_MIA, invalidDate, new ReservationTime(MIA_RESERVATION_TIME), WOOTECO_THEME()))
                .isInstanceOf(BadRequestException.class);
    }

    private static Stream<LocalDate> invalidLocalDate() {
        return Stream.of(
                LocalDate.now(),
                LocalDate.now().minusDays(1L)
        );
    }
}
