package roomescape.business.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ThemeTest {

    @DisplayName("생성자에 null 값이 들어올 수 없다.")
    @ParameterizedTest
    @MethodSource("provideConstructorNullArguments")
    void validateNonNull(
            final String name,
            final String description,
            final String thumbnail
    ) {
        // given & when & then
        assertThatThrownBy(() -> new Theme(name, description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideConstructorNullArguments() {
        return Stream.of(
                Arguments.of(null, "소개", "썸네일"),
                Arguments.of("테마", null, "썸네일"),
                Arguments.of("테마", "소개", null)
        );
    }

    @DisplayName("생성자에 빈 문자열 또는 공백 문자열이 들어올 수 없다.")
    @ParameterizedTest
    @MethodSource("provideConstructorBlankArguments")
    void validateNotBlank(
            final String name,
            final String description,
            final String thumbnail
    ) {
        // given & when & then
        assertThatThrownBy(() -> new Theme(name, description, thumbnail))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideConstructorBlankArguments() {
        return Stream.of(
                Arguments.of("", "소개", "썸네일"),
                Arguments.of("테마", " ", "썸네일"),
                Arguments.of("테마", "소개", "   ")
        );
    }

    @DisplayName("id를 포함하여 생성할 때 id에 null이 들어올 수 없다.")
    @ParameterizedTest
    @MethodSource("provideConstructorArguments")
    void createWithId(
            final String name,
            final String description,
            final String thumbnail
    ) {
        // given & when & then
        assertAll(
                () -> assertThatThrownBy(() -> Theme.createWithId(null, "테마", "소개", "썸네일"))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> Theme.createWithId(1L, name, description, thumbnail))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    private static Stream<Arguments> provideConstructorArguments() {
        return Stream.of(
                Arguments.of(null, "소개", "썸네일"),
                Arguments.of("테마", null, "썸네일"),
                Arguments.of("테마", "소개", null)
        );
    }
}
