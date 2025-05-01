package roomescape.business.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;


class ReservationTest {

    private static final PlayTime VALID_PLAY_TIME = new PlayTime(LocalTime.MAX);
    private static final Theme VALID_THEME = new Theme("테마", "소개", "썸네일");


    @DisplayName("생성자로 null은 들어올 수 없다.")
    @ParameterizedTest
    @MethodSource("provideConstructorArguments")
    void validateNonNull(
            final String name,
            final LocalDate localDate,
            final PlayTime playTime,
            final Theme theme
    ) {
        // given & when & then
        assertThatThrownBy(() -> new Reservation(name, localDate, playTime, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("id를 포함하여 생성할 때 id에 null은 들어올 수 없다.")
    @ParameterizedTest
    @MethodSource("provideConstructorArguments")
    void createWithId(
            final String name,
            final LocalDate localDate,
            final PlayTime playTime,
            final Theme theme
    ) {
        // given & when & then
        assertAll(
                () -> assertThatThrownBy(
                        () -> Reservation.createWithId(null, "hotteok", LocalDate.MAX, VALID_PLAY_TIME, VALID_THEME))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(
                        () -> Reservation.createWithId(1L, name, localDate, playTime, theme))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    private static Stream<Arguments> provideConstructorArguments() {
        return Stream.of(
                Arguments.of(null, LocalDate.MAX, VALID_PLAY_TIME, VALID_THEME),
                Arguments.of("hotteok", null, VALID_PLAY_TIME, VALID_THEME),
                Arguments.of("hotteok", LocalDate.MAX, null, VALID_THEME),
                Arguments.of("hotteok", LocalDate.MAX, VALID_PLAY_TIME, null)
        );
    }

    @DisplayName("이름은 공백이나 빈칸일 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "     "})
    void validateNameIsNotBlack(final String name) {
        // given & when & then
        assertThatThrownBy(() -> new Reservation(name, LocalDate.MAX, VALID_PLAY_TIME, VALID_THEME))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
