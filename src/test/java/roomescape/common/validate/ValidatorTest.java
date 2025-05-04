package roomescape.common.validate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidatorTest {

    @Test
    @DisplayName("필드가 null이면 IllegalArgumentException이 발생한다")
    void validateNotNullFieldThrowsException() {
        // given
        final Validator validator = Validator.of(SampleClass.class);

        // when
        // then
        assertThatThrownBy(() -> validator.notNullField("sampleField", null, "예시 필드"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Validation failed [while checking null]: SampleClass.sampleField");
    }

    @Test
    @DisplayName("필드가 null이 아니면 예외가 발생하지 않는다")
    void validateNotNullFieldPasses() {
        // given
        final Validator validator = Validator.of(SampleClass.class);

        // when
        // then
        assertThat(validator.notNullField("sampleField", "", "예시 필드")).isNotNull();
    }

    @Test
    @DisplayName("blank 검사에서 필드가 null이거나 blank이면 예외가 발생한다")
    void validateNotBlankFieldThrowsException() {
        // given
        final Validator validator = Validator.of(SampleClass.class);

        // when
        // then
        assertThatThrownBy(() -> validator.notBlankField("sampleField", null, "예시 필드"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Validation failed [while checking blank]: SampleClass.sampleField");

        assertThatThrownBy(() -> validator.notBlankField("sampleField", " ", "예시 필드"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Validation failed [while checking blank]: SampleClass.sampleField");
    }

    @Test
    @DisplayName("필드가 blank가 아니면 예외가 발생하지 않는다")
    void validateNotBlankFieldPasses() {
        // given
        final Validator validator = Validator.of(SampleClass.class);

        // when
        // then
        assertThat(validator.notBlankField("sampleField", "content", "예시 필드")).isNotNull();
    }

    static class SampleClass {

        private String sampleField;
    }
}
