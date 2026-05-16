package roomescape.dto.reservationTime;

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

class AddReservationTimeRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("올바른 HH:mm 형식인 경우 검증 통과")
    void success() {
        AddReservationTimeRequest request = new AddReservationTimeRequest("15:30");
        Set<ConstraintViolation<AddReservationTimeRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("시작 시간이 비어있거나 공백이면 검증 실패")
    void blankStartAt(String invalidTime) {
        AddReservationTimeRequest request = new AddReservationTimeRequest(invalidTime);
        Set<ConstraintViolation<AddReservationTimeRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message")
                .contains("시작 시간은 반드시 포함되어야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "15:3",
            "9:30",
            "15-30",
            "오후 3시",
            "1530"
    })
    @DisplayName("HH:mm 형식이 아니면 검증 실패")
    void invalidTimeFormat(String invalidTime) {
        AddReservationTimeRequest request = new AddReservationTimeRequest(invalidTime);
        Set<ConstraintViolation<AddReservationTimeRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message")
                .contains("시작 시간은 24시 형식의 HH:mm 여야 합니다.");
    }

    @Test
    @DisplayName("시작 시간이 null이면 검증 실패")
    void nullStartAt() {
        AddReservationTimeRequest request = new AddReservationTimeRequest(null);
        Set<ConstraintViolation<AddReservationTimeRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message")
                .contains("시작 시간은 반드시 포함되어야 합니다.");
    }
}