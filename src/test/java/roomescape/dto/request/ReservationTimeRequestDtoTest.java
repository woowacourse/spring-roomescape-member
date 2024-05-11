package roomescape.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTimeRequestDtoTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @DisplayName("startAt이 null이나 빈 값이면 검증에 실패한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void notEmptyViolationWithBlankStartAt(String emptyStartAt) {
        ReservationTimeRequestDto reservationTimeRequest = new ReservationTimeRequestDto(emptyStartAt);
        Set<ConstraintViolation<ReservationTimeRequestDto>> violations = validator.validate(reservationTimeRequest);
        assertThat(violations).hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("예약 시간은 null이나 빈 값일 수 없습니다.");
    }
}
