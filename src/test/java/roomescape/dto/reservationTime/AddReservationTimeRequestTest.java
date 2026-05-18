package roomescape.dto.reservationTime;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AddReservationTimeRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("정상 생성 테스트")
    void initTest() {
        AddReservationTimeRequest addReservationTimeRequest = new AddReservationTimeRequest(LocalTime.parse("10:00"));
        Set<ConstraintViolation<AddReservationTimeRequest>> violations = validator.validate(addReservationTimeRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("시간이 null인 경우 실패 테스트")
    void blankTimeTest() {
        AddReservationTimeRequest addReservationTimeRequest = new AddReservationTimeRequest(null);
        Set<ConstraintViolation<AddReservationTimeRequest>> violations = validator.validate(addReservationTimeRequest);

        assertThat(violations).isNotEmpty();
    }

    /**
     * 이건 컨트롤러 단에서 검증
     */
//    @ParameterizedTest
//    @ValueSource(strings = {"9:00", "09:1", "10-00", "12"})
//    @DisplayName("시간이 지정 형식이 아닌 경우 실패 테스트")
//    void invalidTimeTest(String time) {
//        AddReservationTimeRequest addReservationTimeRequest = new AddReservationTimeRequest(LocalTime.parse(time));
//        Set<ConstraintViolation<AddReservationTimeRequest>> violations = validator.validate(addReservationTimeRequest);
//
//        assertThat(violations).hasSize(1);
//    }
}
