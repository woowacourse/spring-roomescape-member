package roomescape.time.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    void 시간_목록을_조회하면_Repository_findAll_결과를_반환한다() {
        List<ReservationTime> times = List.of(new ReservationTime(1L, LocalTime.of(10, 0)));
        when(reservationTimeRepository.findAll()).thenReturn(times);

        List<ReservationTime> result = reservationTimeService.findTimes();

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
    void 시간을_삭제하면_Repository_deleteById에_id를_전달한다() {
        reservationTimeService.deleteTime(3L);

        verify(reservationTimeRepository).deleteById(3L);
    }
}
