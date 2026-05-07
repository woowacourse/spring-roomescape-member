package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeValueTest {

    @Test
    void 테마_id가_null이면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ThemeId(null),
                ErrorCode.INVALID_THEME_ID
        );
    }

    @Test
    void 테마_이름이_null이면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ThemeName(null),
                ErrorCode.INVALID_THEME_NAME
        );
    }

    @Test
    void 테마_이름이_비어있으면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ThemeName(" "),
                ErrorCode.INVALID_THEME_NAME
        );
    }

    @Test
    void 테마_설명이_null이면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ThemeDescription(null),
                ErrorCode.INVALID_THEME_DESCRIPTION
        );
    }

    @Test
    void 테마_설명이_비어있으면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ThemeDescription(" "),
                ErrorCode.INVALID_THEME_DESCRIPTION
        );
    }

    @Test
    void 테마_썸네일이_null이면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ThemeThumbnail(null),
                ErrorCode.INVALID_THEME_THUMBNAIL
        );
    }

    @Test
    void 테마_썸네일이_비어있으면_도메인_예외가_발생한다() {
        assertDomainException(
                () -> new ThemeThumbnail(" "),
                ErrorCode.INVALID_THEME_THUMBNAIL
        );
    }

    private void assertDomainException(Runnable runnable, ErrorCode errorCode) {
        assertThatThrownBy(runnable::run)
                .isInstanceOfSatisfying(DomainException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(errorCode)
                )
                .hasMessage(errorCode.message());
    }
}
