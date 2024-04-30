package roomescape;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.ReservationTimeInput;

@SpringBootTest
public class ReservationTimeServiceTest {

    @Autowired
    ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("유효한 값을 입력하면 예외를 발생하지 않는다.")
    void create_reservationTime() {
        ReservationTimeInput input = new ReservationTimeInput("10:00");
        assertThatCode(() -> reservationTimeService.createReservationTime(input))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유효하지 않은 값을 입력하면 예외를 발생한다.")
    void throw_exception_when_input_is_invalid() {
        ReservationTimeInput input = new ReservationTimeInput("");
        assertThatThrownBy(() -> reservationTimeService.createReservationTime(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
