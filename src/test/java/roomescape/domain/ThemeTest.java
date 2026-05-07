package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidRequestException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeTest {

    @Test
    void 테마_이름이_비어있으면_도메인_예외가_발생한다() {
        assertInvalidRequestException(
                () -> new Theme(" ", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png"),
                "테마 이름은 비어 있을 수 없습니다."
        );
    }

    @Test
    void 테마_설명이_비어있으면_도메인_예외가_발생한다() {
        assertInvalidRequestException(
                () -> new Theme("레벨2 탈출", " ", "https://example.com/theme.png"),
                "테마 설명은 비어 있을 수 없습니다."
        );
    }

    @Test
    void 테마_썸네일이_비어있으면_도메인_예외가_발생한다() {
        assertInvalidRequestException(
                () -> new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", " "),
                "테마 썸네일은 비어 있을 수 없습니다."
        );
    }

    @Test
    void 테마_id가_null이면_도메인_예외가_발생한다() {
        Theme theme = new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");

        assertInvalidRequestException(
                () -> theme.withId(null),
                "테마 id는 비어 있을 수 없습니다."
        );
    }

    @Test
    void 이미_id가_있는_테마에_id를_부여하면_도메인_예외가_발생한다() {
        Theme theme = new Theme(1L, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");

        assertInvalidRequestException(
                () -> theme.withId(2L),
                "이미 id가 존재하는 테마입니다."
        );
    }

    private void assertInvalidRequestException(Runnable runnable, String message) {
        assertThatThrownBy(runnable::run)
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(message);
    }
}
