package roomescape.service.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ThemeRequestTest {

    @DisplayName("이름이 입력되지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throw_exception_when_null_name_input(String name) {
        assertThatThrownBy(() -> new ThemeRequest(name, "하이", "hi.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 반드시 입력되어야 합니다.");
    }

    @DisplayName("설명이 입력되지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throw_exception_when_null_description_input(String description) {
        assertThatThrownBy(() -> new ThemeRequest("아토의 신나는 프로그래밍", description, "hi.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("설명은 반드시 입력되어야 합니다.");
    }

    @DisplayName("썸네일이 입력되지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throw_exception_when_null_thumbnail_input(String thumbnail) {
        assertThatThrownBy(() -> new ThemeRequest("아토의 신나는 프로그래밍", "하이", thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("썸네일은 반드시 입력되어야 합니다.");
    }

}
