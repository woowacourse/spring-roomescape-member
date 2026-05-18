package roomescape.domain.vo;

import org.junit.jupiter.api.Test;
import roomescape.domain.exception.InvalidValueException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DescriptionTest {
    @Test
    void 설명이_공백이면_예외_처리된다() {
        assertThatThrownBy(() -> new Description(""))
                .isInstanceOf(InvalidValueException.class);
    }

    @Test
    void 설명이_500자가_넘으면_예외_처리된다() {
        String invalidValue = "a".repeat(501);

        assertThatThrownBy(() -> new Description(invalidValue))
                .isInstanceOf(InvalidValueException.class);
    }
}