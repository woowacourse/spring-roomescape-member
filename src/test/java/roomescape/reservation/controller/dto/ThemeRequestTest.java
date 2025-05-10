package roomescape.reservation.controller.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.global.exception.error.InvalidRequestException;
import roomescape.reservation.domain.Theme;

class ThemeRequestTest {

    @DisplayName("이름이 존재하지 않거나 공백이면 예외가 발생한다")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void validate_name_test(String name) {
        // given
        String description = "우테코 레벨1를 탈출하는 내용입니다.";
        String thumbnail = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

        // when & then
        assertThatThrownBy(() -> new ThemeRequest(name, description, thumbnail))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("이름은 필수입니다.");
    }

    @DisplayName("설명이 존재하지 않거나 공백이면 예외가 발생한다")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void validate_description_test(String description) {
        // given
        String name = "레벨1 탈출";
        String thumbnail = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

        // when & then
        assertThatThrownBy(() -> new ThemeRequest(name, description, thumbnail))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("설명은 필수입니다.");
    }

    @DisplayName("대표 이미지가 존재하지 않거나 공백이면 예외가 발생한다")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void validate_thumbnail_test(String thumbnail) {
        // given
        String name = "레벨1 탈출";
        String description = "우테코 레벨1를 탈출하는 내용입니다.";

        // when & then
        assertThatThrownBy(() -> new ThemeRequest(name, description, thumbnail))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("대표 이미지는 필수입니다.");
    }

    @DisplayName("ID가 null인 형태로 Theme 객체를 생성한다")
    @Test
    void to_theme_without_id_test() {
        // given
        String name = "레벨1 탈출";
        String description = "우테코 레벨1를 탈출하는 내용입니다.";
        String thumbnail = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

        ThemeRequest themeRequest = new ThemeRequest(name, description, thumbnail);

        // when
        Theme themeWithoutId = themeRequest.toThemeWithoutId();

        // then
        assertThat(themeWithoutId.getId()).isNull();
    }

}
