package roomescape.common.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.common.validation.annotation.NotBlank;
import roomescape.common.validation.validator.FieldBlankValidator;

import java.util.List;

class FieldBlankValidatorTest {

    private final FieldBlankValidator validator = new FieldBlankValidator();

    @Test
    void NotBlank가_붙은_빌드가_비어있으면_예외가_발생한다() {
        String emptyName = " ";
        UseNotBlankDto emtpyNameDto = new UseNotBlankDto(emptyName);

        List<String> errors = validator.validate(emtpyNameDto);

        Assertions.assertThat(errors.getFirst())
                .isEqualTo("name은 비어있을 수 없습니다.");
    }

    @Test
    void NotBlank가_붙은_빌드가_Null이면_예외가_발생한다() {
        String nullName = null;
        UseNotBlankDto nullNameDto = new UseNotBlankDto(nullName);

        List<String> errors = validator.validate(nullNameDto);

        Assertions.assertThat(errors.getFirst())
                .isEqualTo("name은 Null일 수 없습니다.");
    }

    @Test
    void NotBlank가_붙은_빌드가_Strig타입이_아니면_예외가_발생한다() {
        WrongUseNotBlankDto wrongUseNotBlankDto = new WrongUseNotBlankDto(1L);

        List<String> errors = validator.validate(wrongUseNotBlankDto);

        Assertions.assertThat(errors.getFirst())
                .isEqualTo("@NotBlank는 오직 String 타입에만 사용할 수 있습니다.");
    }

    private record UseNotBlankDto(
            @NotBlank(message = "name은 비어있을 수 없습니다.")
            String name
    ){
    }

    private record WrongUseNotBlankDto (
            @NotBlank
            Long id
    ) {
    }

}
