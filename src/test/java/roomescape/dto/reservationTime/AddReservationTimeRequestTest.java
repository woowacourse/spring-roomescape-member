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
        AddReservationTimeRequest addReservationTimeRequest = new AddReservationTimeRequest("10:00");
        Set<ConstraintViolation<AddReservationTimeRequest>> violations = validator.validate(addReservationTimeRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("시간이 공백이면 검증에 실패 테스트")
    void blankTimeTest() {
        AddReservationTimeRequest addReservationTimeRequest = new AddReservationTimeRequest("");
        Set<ConstraintViolation<AddReservationTimeRequest>> violations = validator.validate(addReservationTimeRequest);

        assertThat(violations).hasSize(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"9:00", "09:1", "10-00", "12"})
    @DisplayName("시간이 지정 형식이 아닌 경우 실패 테스트")
    void invalidTimeTest(String time) {
        AddReservationTimeRequest addReservationTimeRequest = new AddReservationTimeRequest(time);
        Set<ConstraintViolation<AddReservationTimeRequest>> violations = validator.validate(addReservationTimeRequest);

        assertThat(violations).hasSize(1);
    }
}
