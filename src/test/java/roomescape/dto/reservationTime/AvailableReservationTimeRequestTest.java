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

class AvailableReservationTimeRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("올바른 날짜 형식과 테마 ID가 전달되면 검증 통과")
    void success() {
        AvailableReservationTimeRequest request = new AvailableReservationTimeRequest("2026-05-13", 1L);
        Set<ConstraintViolation<AvailableReservationTimeRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("날짜가 null이면 검증 실패")
    void nullDate() {
        AvailableReservationTimeRequest request = new AvailableReservationTimeRequest(null, 1L);
        Set<ConstraintViolation<AvailableReservationTimeRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2026/05/13",
            "26-05-13",
            "2026-5-13",
            "2026-05-3",
            "not-a-date"
    })
    @DisplayName("YYYY-MM-DD 형식이 아니면 검증 실패")
    void invalidDateFormat(String invalidDate) {
        AvailableReservationTimeRequest request = new AvailableReservationTimeRequest(invalidDate, 1L);
        Set<ConstraintViolation<AvailableReservationTimeRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("message")
                .contains("날짜는 YYYY-MM-DD 형식이여야 합니다.");
    }
}