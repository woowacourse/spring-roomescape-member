package roomescape.domain.reservation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.global.exception.model.ValidateException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

public class ReservationTest {

    @ParameterizedTest
    @MethodSource("validateConstructorParameterBlankSource")
    @DisplayName("객체 생성 시, null 또는 공백이 존재하면 예외를 발생한다.")
    void validateConstructorParameterBlank(String name, LocalDate date, Time time, Theme theme) {

        // when & then
        Assertions.assertThatThrownBy(() -> new Reservation(name, date, time, theme))
                .isInstanceOf(ValidateException.class);
    }

    static Stream<Arguments> validateConstructorParameterBlankSource() {
        return Stream.of(
                Arguments.of("", LocalDate.now(), new Time(LocalTime.now().plusHours(1)),
                        new Theme("테마명", "설명", "썸네일URI")),
                Arguments.of("", null, new Time(LocalTime.now().plusHours(1)),
                        new Theme("테마명", "설명", "썸네일URI")),
                Arguments.of("", LocalDate.now(), null,
                        new Theme("테마명", "설명", "썸네일URI")),
                Arguments.of("", LocalDate.now(), new Time(LocalTime.now().plusHours(1)), null)
        );
    }
}
