package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.exception.ApiException;
import roomescape.exception.DuplicateException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class AdminReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private AdminReservationTimeService reservationTimeService;

    @Test
    void 예약_시간을_등록할_수_있다() {
        ReservationTime saved = new ReservationTime(1L, LocalTime.of(10, 0));

        when(reservationTimeRepository.save(eq(LocalTime.of(10, 0)))).thenReturn(saved);

        ReservationTime result = reservationTimeService.createReservationTime(LocalTime.of(10, 0));

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 예약_시간이_중복되면_예외가_발생한다() {
        when(reservationTimeRepository.save(any())).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> reservationTimeService.createReservationTime(LocalTime.of(10, 0)))
                .isInstanceOf(DuplicateException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo("이미 존재하는 예약 시간입니다");
    }

    @Test
    void 예약이_있으면_예약_시간을_삭제할_수_없다() {
        when(reservationRepository.countByTimeId(eq(1L))).thenReturn(1);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isInstanceOf(ApiException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo("예약이 있어 삭제할 수 없습니다");
    }

    @Test
    void 예약이_없으면_예약_시간을_삭제할_수_있다() {
        when(reservationRepository.countByTimeId(eq(1L))).thenReturn(0);

        assertThatCode(() -> reservationTimeService.deleteReservationTime(1L))
                .doesNotThrowAnyException();
    }


}
