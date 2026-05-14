package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.dto.reservationTime.AddReservationTimeRequest;
import roomescape.exception.dto.ErrorCode;
import roomescape.exception.exception.DataReferencedException;
import roomescape.exception.exception.DuplicatedResourceException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static roomescape.exception.dto.ErrorCode.DUPLICATED_RESERVATION_TIME;

@ExtendWith(MockitoExtension.class)
public class ReservationTimeServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("동일한 시간이 존재하는 경우 예약 시간 생성 시 예외 테스트")
    void addReservationTimeFailedWhenDuplicatedTest() {
        when(reservationTimeRepository.existsByStartAt(any())).thenReturn(true);

        assertThatThrownBy(() -> reservationTimeService.addReservationTime(
                new AddReservationTimeRequest(LocalTime.parse("10:00"))
        ))
                .isExactlyInstanceOf(DuplicatedResourceException.class)
                .hasMessage(DUPLICATED_RESERVATION_TIME.getMessage());
    }

    @Test
    @DisplayName("정상 삭제 테스트")
    void deleteReservationTimeTest() {
        when(reservationRepository.existsByTimeId(anyLong())).thenReturn(false);

        assertThatCode(() -> reservationTimeService.deleteReservationTime(1))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("외부에서 사용이 되었을 때 삭제 시 예외 테스트")
    void deleteFailedWhenInUseTest() {
        when(reservationRepository.existsByTimeId(anyLong())).thenReturn(true);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(ErrorCode.CANNOT_DELETE_RESERVATION_TIME_IN_USE.getMessage());
    }

    @Test
    @DisplayName("조회 시점엔 없었으나 삭제 시점에 제약조건 위반이 발생한 경우 예외 테스트")
    void deleteFailedByIntegrityTest() {
        when(reservationRepository.existsByTimeId(anyLong())).thenReturn(false);
        doThrow(new DataIntegrityViolationException("정합성 오류"))
                .when(reservationTimeRepository).deleteReservationTime(anyLong());

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(ErrorCode.INTEGRITY_VIOLATION_ON_DELETE.getMessage());
    }
}
