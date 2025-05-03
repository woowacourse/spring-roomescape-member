package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTest {

    @ParameterizedTest
    @MethodSource("reservationConstructorArguments")
    void 예약은_공백이거나_NULL_로_이루어질_수_없다(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        assertThatThrownBy(() -> new Reservation(1L, name, date, reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> reservationConstructorArguments() {
        return Stream.of(
                Arguments.of("", LocalDate.now(),
                        new ReservationTime(1L, LocalTime.now()), new Theme(1L, "테마", "설명", "썸네일")),
                Arguments.of("Hula", null,
                        new ReservationTime(1L, LocalTime.now()), new Theme(1L, "테마", "설명", "썸네일")),
                Arguments.of("Hula", LocalDate.now(),
                        null, new Theme(1L, "테마", "설명", "썸네일")),
                Arguments.of("Hula", LocalDate.now(),
                        new ReservationTime(1L, LocalTime.now()), null)
        );
    }
}

