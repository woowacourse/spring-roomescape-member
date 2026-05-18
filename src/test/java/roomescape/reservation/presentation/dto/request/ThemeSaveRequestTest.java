package roomescape.reservation.presentation.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ThemeSaveRequestTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void 유효한_테마_요청은_검증에_성공한다() {
        ThemeSaveRequest request = new ThemeSaveRequest(
                "무서운게 딱 좋아",
                "무서운 분위기의 방탈출",
                "https://example.com/theme.jpg"
        );

        Set<ConstraintViolation<ThemeSaveRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void 이름이_비어있으면_검증에_실패한다() {
        ThemeSaveRequest request = new ThemeSaveRequest(
                "",
                "무서운 분위기의 방탈출",
                "https://example.com/theme.jpg"
        );

        Set<ConstraintViolation<ThemeSaveRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("name");
    }
}
