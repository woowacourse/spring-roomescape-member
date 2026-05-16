package roomescape.dto.theme;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PopularConditionRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName(" 올바른 날짜 형식과 size가 전달되면 검증 통과")
    void success() {
        PopularConditionRequest request = new PopularConditionRequest("2026-05-01", "2026-05-13", 5L);
        Set<ConstraintViolation<PopularConditionRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026/05/01", "26-05-01", "2026-5-1", "not-a-date"})
    @DisplayName("시작 날짜 형식이 YYYY-MM-DD가 아니면 검증 실패")
    void invalidStartDate(String startDate) {
        PopularConditionRequest request = new PopularConditionRequest(startDate, "2026-05-13", 5L);
        Set<ConstraintViolation<PopularConditionRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message")
                .contains("날짜는 YYYY-MM-DD 형식이여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026.05.13", "2026-05-3", "2026-1-13"})
    @DisplayName("종료 날짜 형식이 YYYY-MM-DD가 아니면 검증 실패")
    void invalidEndDate(String endDate) {
        PopularConditionRequest request = new PopularConditionRequest("2026-05-01", endDate, 5L);
        Set<ConstraintViolation<PopularConditionRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message")
                .contains("날짜는 YYYY-MM-DD 형식이여야 합니다.");
    }
}