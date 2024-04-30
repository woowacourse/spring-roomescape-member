package roomescape.time.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("존재하는 아이디가 없으면 예외를 발생한다.")
    void findByIdExceptionTest() {
        Long timeId = 1L;

        doReturn(Optional.empty()).when(reservationTimeRepository)
                .findById(timeId);

        assertThatThrownBy(() -> reservationTimeService.findById(timeId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 해당 시간으로 예약 되있을 경우 삭제 시 예외가 발생한다.")
    void deleteExceptionTest() {
        Long timeId = 1L;

        doReturn(Optional.of(new ReservationTime(timeId, LocalTime.now()))).when(reservationTimeRepository)
                .findReservationInSameId(timeId);

        assertThatThrownBy(() -> reservationTimeService.delete(timeId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
