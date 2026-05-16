package roomescape.reservation.presentation.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTimeSaveRequestTest {

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
    void 유효한_예약_시간_요청은_검증에_성공한다() {
        ReservationTimeSaveRequest request = new ReservationTimeSaveRequest(LocalTime.of(14, 0));

        Set<ConstraintViolation<ReservationTimeSaveRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void 시작_시간이_null이면_검증에_실패한다() {
        ReservationTimeSaveRequest request = new ReservationTimeSaveRequest(null);

        Set<ConstraintViolation<ReservationTimeSaveRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("startAt");
    }
}
