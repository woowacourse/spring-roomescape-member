package roomescape.common.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.common.validation.annotation.NotNull;
import roomescape.common.validation.validator.NotNullValidator;

import java.util.List;

class NotNullValidatorTest {

    private final NotNullValidator notNullValidator = new NotNullValidator();

    @Test
    void NotNull이_붙은_필드가_Null이면_예외가_발생한다() {
        Long nullId = null;
        UseNotNullDto useNotNullDto = new UseNotNullDto(nullId);

        List<String> errors = notNullValidator.validate(useNotNullDto);

        Assertions.assertThat(errors.getFirst())
                .isEqualTo("id는 Null일 수 없습니다.");
    }

    private record UseNotNullDto(
            @NotNull(message = "id는 Null일 수 없습니다.")
            Long id
    ) {
    }

}
