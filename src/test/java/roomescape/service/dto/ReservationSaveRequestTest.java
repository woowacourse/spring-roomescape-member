//package roomescape.service.dto;
//
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.NullAndEmptySource;
//
//import java.time.LocalDate;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class ReservationSaveRequestTest {
//
//    private static ValidatorFactory factory;
//    private static Validator validator;
//
//    @BeforeAll
//    public static void init() {
//        factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//    @DisplayName("이름이 정상 입력될 경우 성공한다.")
//    void checkNameBlank_Success() {
//        // given
//        ReservationSaveRequest request = new ReservationSaveRequest("naknak", LocalDate.now(), 1L, 1L);
//
//        // when
//        Set<ConstraintViolation<ReservationSaveRequest>> violations = validator.validate(request);
//
//        // then
//        assertThat(violations).isEmpty();
//    }
//
//    @ParameterizedTest
//    @NullAndEmptySource
//    @DisplayName("이름이 빈칸인 경우 예외가 발생한다.")
//    void checkNameBlank_Failure(String name) {
//        // given
//        ReservationSaveRequest request = new ReservationSaveRequest("", LocalDate.now(), 1L, 1L);
//
//        // when
//        Set<ConstraintViolation<ReservationSaveRequest>> violations = validator.validate(request);
//        Optional<String> exceptionMessage = violations.stream()
//                .map(ConstraintViolation::getMessage)
//                .findFirst();
//
//        // then
//        assertThat(exceptionMessage).contains("예약자 이름은 빈칸일 수 없습니다.");
//    }
//}
