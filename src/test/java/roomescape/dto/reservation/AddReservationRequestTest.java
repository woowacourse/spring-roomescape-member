package roomescape.dto.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AddReservationRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("정상 생성 테스트")
    void initTest() {
        AddReservationRequest addReservationRequest = new AddReservationRequest("브라운", LocalDate.parse("2023-08-15"), 1L, 1L);
        Set<ConstraintViolation<AddReservationRequest>> violations = validator.validate(addReservationRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이름이 공백인 경우 실패 테스트")
    void blankNameTest() {
        AddReservationRequest addReservationRequest = new AddReservationRequest("", LocalDate.parse("2023-08-15"), 1L, 1L);
        Set<ConstraintViolation<AddReservationRequest>> violations = validator.validate(addReservationRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("날짜가 null인 경우 실패 테스트")
    void nullDateTest() {
        AddReservationRequest request = new AddReservationRequest("브라운", null, 1L, 1L);

        Set<ConstraintViolation<AddReservationRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
    }

    /**
     * 이건 contoller 테스트로 옮겨야됨
     * LocalDate 포맷이 다른 경우에는 DTO까지 들어오지도 않음
     */
//    @ParameterizedTest
//    @ValueSource(strings = {"20230815", "23-08-15", "2023-8-15", "2023-08-5", "2023/08/15", "23/8/15"})
//    @DisplayName("날짜 지정 형식이 아닌 경우 실패 테스트")
//    void invalidTimeTest(String date) {
//        AddReservationRequest addReservationRequest = new AddReservationRequest("브라운", date, 1L, 1L);
//        Set<ConstraintViolation<AddReservationRequest>> violations = validator.validate(addReservationRequest);
//
//        assertThat(violations).hasSize(1);
//    }
}
