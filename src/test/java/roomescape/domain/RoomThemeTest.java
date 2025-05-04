package roomescape.domain;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.exception.custom.InvalidInputException;

class RoomThemeTest {

    private static Stream<Arguments> provideInvalidValue() {
        return Stream.of(
                Arguments.of(null, "description", "thumbnail"),
                Arguments.of("   ", "description", "thumbnail"),
                Arguments.of("theme", null, "thumbnail"),
                Arguments.of("theme", "   ", "thumbnail"),
                Arguments.of("theme", "description", null),
                Arguments.of("theme", "description", "   ")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidValue")
    @DisplayName("유효하지 않은 값으로 생성 시, 예외를 던진다")
    void throwExceptionWhenInvalidValue(final String name,
                                        final String description,
                                        final String thumbnail) {
        //when & then
        Assertions.assertThatThrownBy(() -> new RoomTheme(name, description, thumbnail))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("빈 값이 입력될 수 없습니다");
    }
}
