package roomescape.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.request.ReservationTimeCreateRequest;

import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationTimeCreateRequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("ReservationTimeCreateRequest 생성 테스트")
    void generateReservationTimeCreateRequest() {
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.now());
        Set<ConstraintViolation<ReservationTimeCreateRequest>> violations = validator.validate(request);
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("ReservationTimeCreateRequest startAt 빈 값인 경우 예외 처리")
    void invalidTime() {
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(null);
        Set<ConstraintViolation<ReservationTimeCreateRequest>> violations = validator.validate(request);
        for (ConstraintViolation<ReservationTimeCreateRequest> violation : violations) {
            assertThat(violation.getMessage()).isEqualTo("예약 시간은 빈 값일 수 없습니다");
        }
    }
}
