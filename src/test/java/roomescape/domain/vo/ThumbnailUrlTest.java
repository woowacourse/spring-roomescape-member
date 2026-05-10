package roomescape.domain.vo;

import org.junit.jupiter.api.Test;
import roomescape.common.exception.DomainException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThumbnailUrlTest {
    @Test
    void URL이_공백이면_예외_처리된다() {
        assertThatThrownBy(() -> new ThumbnailUrl(""))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void URL이_500자가_넘으면_예외_처리된다() {
        String invalidValue = "https://test1.com" + "a".repeat(501);

        assertThatThrownBy(() -> new ThumbnailUrl(invalidValue))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void URL이_http_양식을_지키지_않으면_예외_처리한다() {
        String invalidValue = "a".repeat(12);

        assertThatThrownBy(() -> new ThumbnailUrl(invalidValue))
                .isInstanceOf(DomainException.class);
    }
}