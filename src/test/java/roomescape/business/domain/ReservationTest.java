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


class ReservationTest {

    private static final User VALID_USER = new User("hotteok", "qq@example.com", "qwe123", Role.USER);
    private static final PlayTime VALID_PLAY_TIME = new PlayTime(LocalTime.MAX);
    private static final Theme VALID_THEME = new Theme("테마", "소개", "썸네일");


    @DisplayName("생성자로 null은 들어올 수 없다.")
    @ParameterizedTest
    @MethodSource("provideConstructorArguments")
    void validateNonNull(
            final User user,
            final LocalDate localDate,
            final PlayTime playTime,
            final Theme theme
    ) {
        // given & when & then
        assertThatThrownBy(() -> new Reservation(user, localDate, playTime, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("id를 포함하여 생성할 때 id에 null은 들어올 수 없다.")
    @ParameterizedTest
    @MethodSource("provideConstructorArguments")
    void createWithId(
            final User user,
            final LocalDate localDate,
            final PlayTime playTime,
            final Theme theme
    ) {
        // given & when & then
        assertAll(
                () -> assertThatThrownBy(
                        () -> Reservation.createWithId(null, VALID_USER, LocalDate.MAX, VALID_PLAY_TIME, VALID_THEME))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(
                        () -> Reservation.createWithId(1L, user, localDate, playTime, theme))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    private static Stream<Arguments> provideConstructorArguments() {
        return Stream.of(
                Arguments.of(null, LocalDate.MAX, VALID_PLAY_TIME, VALID_THEME),
                Arguments.of(VALID_USER, null, VALID_PLAY_TIME, VALID_THEME),
                Arguments.of(VALID_USER, LocalDate.MAX, null, VALID_THEME),
                Arguments.of(VALID_USER, LocalDate.MAX, VALID_PLAY_TIME, null)
        );
    }
}
