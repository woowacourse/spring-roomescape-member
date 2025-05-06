package roomescape.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.dto.request.ReservationCreateRequest;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class ReservationCreateRequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("ReservationCreateRequest 정상 생성 테스트")
    void generateReservationCreateRequest() {
        ReservationCreateRequest request = new ReservationCreateRequest("홍길동", LocalDate.now(), 1L, 1L);
        Set<ConstraintViolation<ReservationCreateRequest>> violations = validator.validate(request);
        assertThat(violations.size()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("getEmptyNameReservationCreateRequests")
    @DisplayName("ReservationCreateRequest Name 빈 값인 경우 예외 처리")
    void invalidName(ReservationCreateRequest request) {
        Set<ConstraintViolation<ReservationCreateRequest>> violations = validator.validate(request);
        for (ConstraintViolation<ReservationCreateRequest> violation : violations) {
            assertThat(violation.getMessage()).isEqualTo("이름은 빈 값이 올 수 없습니다");
        }
    }

    static Stream<Arguments> getEmptyNameReservationCreateRequests() {
        return Stream.of(
                Arguments.arguments(new ReservationCreateRequest(null, LocalDate.now(), 1L, 1L)),
                Arguments.arguments(new ReservationCreateRequest("", LocalDate.now(), 1L, 1L)),
                Arguments.arguments(new ReservationCreateRequest(" ", LocalDate.now(), 1L, 1L))
        );
    }

    @Test
    @DisplayName("ReservationCreateRequest Date 빈 값인 경우 예외 처리")
    void invalidDate() {
        ReservationCreateRequest request = new ReservationCreateRequest("홍길동", null, 1L, 1L);
        Set<ConstraintViolation<ReservationCreateRequest>> violations = validator.validate(request);
        for (ConstraintViolation<ReservationCreateRequest> violation : violations) {
            assertThat(violation.getMessage()).isEqualTo("예약 날짜는 빈 값이 올 수 없습니다");
        }
    }

    @Test
    @DisplayName("ReservationCreateRequest time 빈 값인 경우 예외 처리")
    void invalidTime() {
        ReservationCreateRequest request = new ReservationCreateRequest("홍길동", LocalDate.now(), null, 1L);
        Set<ConstraintViolation<ReservationCreateRequest>> violations = validator.validate(request);
        for (ConstraintViolation<ReservationCreateRequest> violation : violations) {
            assertThat(violation.getMessage()).isEqualTo("예약 시간이 올바르지 않습니다");
        }
    }

    @Test
    @DisplayName("ReservationCreateRequest theme 빈 값인 경우 예외 처리")
    void invalidTheme() {
        ReservationCreateRequest request = new ReservationCreateRequest("홍길동", LocalDate.now(), 1L, null);
        Set<ConstraintViolation<ReservationCreateRequest>> violations = validator.validate(request);
        for (ConstraintViolation<ReservationCreateRequest> violation : violations) {
            assertThat(violation.getMessage()).isEqualTo("예약 테마가 올바르지 않습니다");
        }
    }
}
