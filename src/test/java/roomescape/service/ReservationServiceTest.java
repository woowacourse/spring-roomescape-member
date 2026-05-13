package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.config.FixedClockConfig.FUTURE_DATE;
import static roomescape.config.FixedClockConfig.TODAY;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.request.ReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("존재하지 않는 timeId로 예약하면 예외가 발생한다")
    void save_fail_invalid_time_id() {
        Long invalidTimeId = 999L;
        ReservationRequest request = new ReservationRequest(
                "아나키",
                LocalDate.parse(FUTURE_DATE),
                invalidTimeId,
                1L
        );

        assertThatThrownBy(() -> reservationService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 시간입니다.");
    }

    @Test
    @DisplayName("이미 존재하는 예약 건과 중복으로 예약하면 예외가 발생한다")
    void throwsException_whenDuplicateReservationExists() {
        LocalDate date = LocalDate.parse(TODAY);

        ReservationRequest request = new ReservationRequest("아나키", date, 1L, 11L);

        assertThatThrownBy(() -> reservationService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 예약 건입니다.");
    }

    @Test
    @DisplayName("중복되지 않는 시간에 예약을 하면 통과한다.")
    void 중복이_없는_정상_예약_테스트() {
        ReservationRequest request = new ReservationRequest("아나키", LocalDate.of(2026, 5, 14), 1L, 1L);

        assertDoesNotThrow(() -> reservationService.save(request));
    }
}
