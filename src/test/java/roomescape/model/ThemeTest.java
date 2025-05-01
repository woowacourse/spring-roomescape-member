package roomescape.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ThemeTest {

    @ParameterizedTest
    @MethodSource("nullValues")
    @DisplayName("테마 생성 시 id가 아닌 모든 값들이 존재하지 않으면 예외가 발생한다")
    void anyValueNullException(String name, String description, String thumbnail) {
        // given & when & then
        assertThatThrownBy(() -> new Theme(1L, name, description, thumbnail))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이름이 10자 초과이면 예외가 발생한다")
    void nameLengthException() {
        // given & when & then
        assertThatThrownBy(() -> new Theme(
            1L,
            "가".repeat(11),
            "우테코 레벨2를 탈출하는 내용입니다.",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("설명이 50자 초과이면 예외가 발생한다")
    void descriptionLengthException() {
        // given & when & then
        assertThatThrownBy(() -> new Theme(
            1L,
            "레벨2 탈출",
            "가".repeat(51),
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        )).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> nullValues() {
        return Stream.of(
            Arguments.of(
                null,
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
            ),
            Arguments.of(
                "레벨2 탈출",
                null,
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
            ),
            Arguments.of(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                null
            )
        );
    }
}
