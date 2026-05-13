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

class UpdateReservationRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든 필드가 유효하면 검증 통과")
    void success() {
        UpdateReservationRequest request = new UpdateReservationRequest(1L, "브라운", "2026-05-13", 1L, 1L);
        Set<ConstraintViolation<UpdateReservationRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("예약 ID가 null이면 검증 실패")
    void nullId() {
        UpdateReservationRequest request = new UpdateReservationRequest(null, "브라운", "2026-05-13", 1L, 1L);
        Set<ConstraintViolation<UpdateReservationRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").contains("예약 ID가 반드시 포함되어야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("이름이 비어있거나 공백이면 검증 실패")
    void invalidName(String name) {
        UpdateReservationRequest request = new UpdateReservationRequest(1L, name, "2026-05-13", 1L, 1L);
        Set<ConstraintViolation<UpdateReservationRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("이름이 반드시 포함되어야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026/05/13", "26-05-13", "2026-5-13", "not-date"})
    @DisplayName("날짜 형식이 YYYY-MM-DD가 아니면 검증 실패")
    void invalidDateFormat(String date) {
        UpdateReservationRequest request = new UpdateReservationRequest(1L, "브라운", date, 1L, 1L);
        Set<ConstraintViolation<UpdateReservationRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message").contains("날짜는 YYYY-MM-DD 형식이여야 합니다.");
    }

    @Test
    @DisplayName("시간 ID 또는 테마 ID가 null이면 검증 실패")
    void nullIds() {
        UpdateReservationRequest request = new UpdateReservationRequest(1L, "브라운", "2026-05-13", null, null);
        Set<ConstraintViolation<UpdateReservationRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(2);
        assertThat(violations).extracting("message")
                .containsExactlyInAnyOrder("시간 ID가 반드시 포함되어야 합니다.", "테마 ID가 반드시 포함되어야 합니다.");
    }
}