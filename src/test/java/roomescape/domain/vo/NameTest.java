package roomescape.domain.vo;

import org.junit.jupiter.api.Test;
import roomescape.common.exception.DomainException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NameTest {
    @Test
    void 이름이_공백이면_예외_처리된다() {
        assertThatThrownBy(() -> new Name(""))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void 이름이_50자가_넘으면_예외_처리된다() {
        String invalidValue = "a".repeat(51);

        assertThatThrownBy(() -> new Name(invalidValue))
                .isInstanceOf(DomainException.class);
    }
}
