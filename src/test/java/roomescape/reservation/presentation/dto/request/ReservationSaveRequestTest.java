package roomescape.reservation.presentation.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationSaveRequestTest {

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
    void 유효한_예약_요청은_검증에_성공한다() {
        ReservationSaveRequest request = new ReservationSaveRequest(
                "브라운",
                LocalDate.of(2026, 5, 6),
                1L,
                2L
        );

        Set<ConstraintViolation<ReservationSaveRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void 이름이_비어있으면_검증에_실패한다() {
        ReservationSaveRequest request = new ReservationSaveRequest(
                "",
                LocalDate.of(2026, 5, 6),
                1L,
                2L
        );

        Set<ConstraintViolation<ReservationSaveRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("name");
    }

    @Test
    void 날짜가_null이면_검증에_실패한다() {
        ReservationSaveRequest request = new ReservationSaveRequest(
                "브라운",
                null,
                1L,
                2L
        );

        Set<ConstraintViolation<ReservationSaveRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("date");
    }

    @Test
    void timeId가_null이면_검증에_실패한다() {
        ReservationSaveRequest request = new ReservationSaveRequest(
                "브라운",
                LocalDate.of(2026, 5, 6),
                null,
                2L
        );

        Set<ConstraintViolation<ReservationSaveRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("timeId");
    }

    @Test
    void themeId가_null이면_검증에_실패한다() {
        ReservationSaveRequest request = new ReservationSaveRequest(
                "브라운",
                LocalDate.of(2026, 5, 6),
                1L,
                null
        );

        Set<ConstraintViolation<ReservationSaveRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("themeId");
    }
}
