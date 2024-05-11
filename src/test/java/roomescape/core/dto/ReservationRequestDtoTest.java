package roomescape.core.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.util.Fixture.DATE;
import static roomescape.util.Fixture.ID;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationRequestDtoTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @DisplayName("date가 null이나 빈 값이면 검증에 실패한다")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n"})
    void notEmptyViolationWithBlankDate(final String emptyDate) {
        final ReservationRequestDto reservationRequest = new ReservationRequestDto(emptyDate, ID, ID);
        final Set<ConstraintViolation<ReservationRequestDto>> violations = validator.validate(reservationRequest);
        assertThat(violations).hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("예약 날짜는 null이나 빈 값일 수 없습니다.");
    }

    @Test
    @DisplayName("timeId가 null이면 검증에 실패한다")
    void notEmptyViolationWithNullTimeId() {
        final ReservationRequestDto reservationRequest = new ReservationRequestDto(DATE, null, ID);
        final Set<ConstraintViolation<ReservationRequestDto>> violations = validator.validate(reservationRequest);
        assertThat(violations).hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("예약 시간 id는 null일 수 없습니다.");
    }

    @Test
    @DisplayName("themeId가 null이면 검증에 실패한다")
    void notEmptyViolationWithNullThemeId() {
        final ReservationRequestDto reservationRequest = new ReservationRequestDto(DATE, ID, null);
        final Set<ConstraintViolation<ReservationRequestDto>> violations = validator.validate(reservationRequest);
        assertThat(violations).hasSize(1)
                .extracting(ConstraintViolation::getMessage)
                .first()
                .isEqualTo("예약 테마 id는 null일 수 없습니다.");
    }
}
