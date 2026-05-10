package roomescape.domain.vo;

import org.junit.jupiter.api.Test;
import roomescape.common.exception.DomainException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DescriptionTest {
    @Test
    void 설명이_공백이면_예외_처리된다() {
        assertThatThrownBy(() -> new Description(""))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void 설명이_500자가_넘으면_예외_처리된다() {
        String invalidValue = "a".repeat(501);

        assertThatThrownBy(() -> new Description(invalidValue))
                .isInstanceOf(DomainException.class);
    }
}