package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ThemeTest {

    @DisplayName("필수 값에 null이 들어가면 예외가 발생한다")
    @MethodSource
    @ParameterizedTest
    void validate_test(String name, String description, String thumbnail, String errorMessage) {
        // given
        Long id = 1L;

        // when & then
        assertThatThrownBy(() -> new Theme(id, name, description, thumbnail))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(errorMessage);
    }

    @DisplayName("id가 포함된 객체를 반환한다")
    @Test
    void with_id_test() {
        // given
        String name = "레벨1 탈출";
        String description = "우테코 레벨1를 탈출하는 내용입니다.";
        String thumbnail = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";
        Long id = 3L;
        Theme themeWithoutId = new Theme(null, name, description, thumbnail);

        // when
        Theme themeWithId = themeWithoutId.withId(id);

        // then
        assertThat(themeWithId.getId()).isEqualTo(id);
    }

    static Stream<Arguments> validate_test() {
        return Stream.of(
                Arguments.of(null, "우테코 레벨1를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg",
                        "테마 이름은 null일 수 없습니다."),
                Arguments.of("레벨1 탈출", null, "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg",
                        "테마 설명은 null일 수 없습니다."),
                Arguments.of("레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.", null, "테마 대표 이미지는 null일 수 없습니다.")
        );
    }

}
