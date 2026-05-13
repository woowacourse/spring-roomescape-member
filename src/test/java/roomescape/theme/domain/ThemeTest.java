package roomescape.theme.domain;

import org.junit.jupiter.api.Test;
import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeTest {

    @Test
    void 유효한_값으로_테마를_생성하면_필드가_저장된다() {
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        assertThat(theme.getId()).isEqualTo(1L);
        assertThat(theme.getName()).isEqualTo("공포방");
        assertThat(theme.getDescription()).isEqualTo("무서운방입니다.");
        assertThat(theme.getThumbnail()).isEqualTo("image-url");
    }

    @Test
    void 테마_이름이_null이면_BusinessException이_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, null, "설명", "image-url"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    void 테마_이름이_빈_문자열이면_BusinessException이_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, "", "설명", "image-url"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    void 테마_이름이_공백만_있으면_BusinessException이_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, "   ", "설명", "image-url"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    void 테마_설명이_null이면_BusinessException이_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, "공포방", null, "image-url"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    void 테마_설명이_빈_문자열이면_BusinessException이_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, "공포방", "", "image-url"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    void 테마_썸네일이_null이면_BusinessException이_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, "공포방", "설명", null))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

    @Test
    void 테마_썸네일이_빈_문자열이면_BusinessException이_발생한다() {
        assertThatThrownBy(() -> new Theme(1L, "공포방", "설명", ""))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_INPUT);
    }

}
