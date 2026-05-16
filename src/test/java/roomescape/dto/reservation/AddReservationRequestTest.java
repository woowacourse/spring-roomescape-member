package roomescape.dto.reservation;

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

class AddReservationRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든 필드가 유효하면 검증 통과")
    void success() {
        AddReservationRequest request = new AddReservationRequest("브라운", "2026-05-13", 1L, 1L);
        Set<ConstraintViolation<AddReservationRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("이름이 비어있거나 공백이면 검증 실패")
    void invalidName(String name) {
        AddReservationRequest request = new AddReservationRequest(name, "2026-05-13", 1L, 1L);
        Set<ConstraintViolation<AddReservationRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("이름이 반드시 포함되어야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026/05/13", "2026-5-8", "26-05-13", "May 13, 2026"})
    @DisplayName("날짜 형식이 YYYY-MM-DD가 아니면 검증 실패")
    void invalidDateFormat(String date) {
        AddReservationRequest request = new AddReservationRequest("브라운", date, 1L, 1L);
        Set<ConstraintViolation<AddReservationRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("날짜는 YYYY-MM-DD 형식이여야 합니다.");
    }

    @Test
    @DisplayName("시간 ID가 없으면 검증 실패")
    void nullTimeId() {
        AddReservationRequest request = new AddReservationRequest("브라운", "2026-05-13", null, 1L);
        Set<ConstraintViolation<AddReservationRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("시간 ID가 반드시 포함되어야 합니다.");
    }

    @Test
    @DisplayName("테마 ID가 없으면 검증 실패")
    void nullThemeId() {
        AddReservationRequest request = new AddReservationRequest("브라운", "2026-05-13", 1L, null);
        Set<ConstraintViolation<AddReservationRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("테마 ID가 반드시 포함되어야 합니다.");
    }
}