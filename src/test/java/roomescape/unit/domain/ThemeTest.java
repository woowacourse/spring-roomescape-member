package roomescape.unit.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ThemeTest {

    @ParameterizedTest
    @MethodSource("themeConstructorArguments")
    void 테마는_공백이거나_NULL_로_이루어질_수_없다(String name, String description, String thumbnail) {
        assertThatThrownBy(() -> new Theme(1L, name, description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> themeConstructorArguments() {
        return Stream.of(
                Arguments.arguments("", "description", "thumbnail"),
                Arguments.arguments("pppk", null, "thumbnail"),
                Arguments.arguments("pppk", "description", null)
        );
    }
}

