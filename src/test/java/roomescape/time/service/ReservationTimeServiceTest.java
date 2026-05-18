package roomescape.time.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    void 시간_목록을_조회하면_Repository_findAll_결과를_반환한다() {
        List<ReservationTime> times = List.of(new ReservationTime(1L, LocalTime.of(10, 0)));
        when(reservationTimeRepository.findAll()).thenReturn(times);

        List<ReservationTime> result = reservationTimeService.getTimes();

        verify(reservationTimeRepository).findAll();
        assertThat(result).isSameAs(times);
    }

    @Test
    void 시간을_생성하면_시작_시간을_Repository_save에_전달하고_결과를_반환한다() {
        ReservationTime saved = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeRepository.save(any())).thenReturn(saved);

        ReservationTime result = reservationTimeService.createTime(LocalTime.of(10, 0));

        verify(reservationTimeRepository).save(any());
        assertThat(result).isSameAs(saved);
    }

    @Test
    void 예약이_없는_시간은_삭제_가능하다() {
        when(reservationRepository.existsByTimeId(any()))
                .thenReturn(false);

        reservationTimeService.removeTime(1L);

        verify(reservationTimeRepository).remove(1L);
    }

    @Test
    void 예약이_있는_시간을_삭제하면_예외를_발생하고_삭제하지_않는다() {
        when(reservationRepository.existsByTimeId(any()))
                .thenReturn(true);

        assertThatThrownBy(() -> reservationTimeService.removeTime(3L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_TIME_IN_USE);

        verify(reservationTimeRepository, never()).remove(any());
    }
}
