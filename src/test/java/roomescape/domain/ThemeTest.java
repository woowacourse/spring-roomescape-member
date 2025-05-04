package roomescape.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThemeTest {

    @Test
    void 테마_이름이_null이면_예외를_발생시킨다() {
        assertThatThrownBy(() -> {
            new Theme(
                    null,
                    "탈출 설명",
                    "abc.jpg");
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    void 테마_썰명이_null이면_예외를_발생시킨다() {
        assertThatThrownBy(() -> {
            new Theme(
                    "탈출",
                    null,
                    "abc.jpg");
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    void 썸네일이_null이면_예외를_발생시킨다() {
        assertThatThrownBy(() -> {
            new Theme(
                    "탈출",
                    "탈출 설명",
                    null);
        }).isInstanceOf(NullPointerException.class);
    }
}
