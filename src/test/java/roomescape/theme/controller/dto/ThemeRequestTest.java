package roomescape.theme.controller.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.theme.controller.dto.ThemeRequest;

public class ThemeRequestTest {

    @DisplayName("이름이 공백이면 예외가 발생한다")
    @Test
    void name_blank_exception() {
        // when && then
        assertThatThrownBy(() -> new ThemeRequest(" ","우테코 레벨1를 탈출하는 내용입니다.","https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg" ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 필수입니다.");
    }

    @DisplayName("설명이 공백이면 예외가 발생한다")
    @Test
    void description_blank_exception() {
        // when && then
        assertThatThrownBy(() -> new ThemeRequest("레벨1 탈출"," ","https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("설명은 필수입니다.");
    }

    @DisplayName("대표 이미지가 공백이면 예외가 발생한다")
    @Test
    void thumbnail_blank_exception() {
        // when && then
        assertThatThrownBy(() -> new ThemeRequest("레벨1 탈출","우테코 레벨1를 탈출하는 내용입니다."," "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("대표 이미지는 필수입니다.");
    }

}
