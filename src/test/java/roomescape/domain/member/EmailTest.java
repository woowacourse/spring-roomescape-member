package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class EmailTest {
    @Test
    void 이메일을_생성한다() {
        String email = "prin@gmail.com";

        Email actual = new Email(email);

        assertThat(actual.getValue()).isEqualTo(email);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 이메일이_비어있으면_예외가_발생한다(String email) {
        assertThatThrownBy(() -> new Email(email))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일이 비어있습니다.");
    }

    @Test
    void 이메일이_최소_길이보다_짧으면_예외가_발생한다() {
        String email = "a@b.";
        assertThatThrownBy(() -> new Email(email))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일은 5자 이상, 30자 이하여야 합니다.");
    }

    @Test
    void 이메일이_최대_길이보다_길면_예외가_발생한다() {
        String email = "prinprinprinprinprinprin@gmail.com";

        assertThatThrownBy(() -> new Email(email))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일은 5자 이상, 30자 이하여야 합니다.");
    }

    @Test
    void 이메일이_형식에_맞지_않으면_예외가_발생한다() {
        String email = "prin@com";

        assertThatThrownBy(() -> new Email(email))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일 형식이 올바르지 않습니다.");
    }
}
