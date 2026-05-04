package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.ReservationDao;
import roomescape.dto.request.ReservationRequest;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("존재하지 않는 timeId로 예약하면 예외가 발생한다")
    void save_fail_invalid_time_id() {
        // given
        Long invalidTimeId = 999L;
        ReservationRequest request = new ReservationRequest("아나키", LocalDate.of(2026,5,4), invalidTimeId);

        // when & then
        when(reservationDao.findTimeById(invalidTimeId)).thenReturn(null);
        assertThatThrownBy(() -> reservationService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("요청하신 시간 ID가 존재하지 않습니다.");
    }
}
