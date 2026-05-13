package roomescape.theme.domain;

import org.junit.jupiter.api.Test;
import roomescape.global.exception.DomainNotValidValueException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeTest {

    @Test
    void 테마_이름이_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, null, "설명", "image-url"))
                .isInstanceOf(DomainNotValidValueException.class)
                .hasMessage("테마 이름은 비어있을 수 없습니다.");
    }

    @Test
    void 테마_이름이_빈_문자열이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, "", "설명", "image-url"))
                .isInstanceOf(DomainNotValidValueException.class)
                .hasMessage("테마 이름은 비어있을 수 없습니다.");
    }

    @Test
    void 테마_설명이_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, "공포방", null, "image-url"))
                .isInstanceOf(DomainNotValidValueException.class)
                .hasMessage("테마 설명은 비어있을 수 없습니다.");
    }

    @Test
    void 테마_설명이_빈_문자열이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, "공포방", "", "image-url"))
                .isInstanceOf(DomainNotValidValueException.class)
                .hasMessage("테마 설명은 비어있을 수 없습니다.");
    }

    @Test
    void 테마_썸네일이_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, "공포방", "설명", null))
                .isInstanceOf(DomainNotValidValueException.class)
                .hasMessage("테마 썸네일은 비어있을 수 없습니다.");
    }

    @Test
    void 테마_썸네일이_빈_문자열이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, "공포방", "설명", ""))
                .isInstanceOf(DomainNotValidValueException.class)
                .hasMessage("테마 썸네일은 비어있을 수 없습니다.");
    }
}
