package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock private ReservationTimeDao reservationTimeDao;
    @Mock private ReservationDao reservationDao;
    @InjectMocks private ReservationTimeService reservationTimeService;

    @Test
    void findAll_전체_조회() {
        List<ReservationTime> times = List.of(
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new ReservationTime(2L, LocalTime.of(11, 0))
        );
        given(reservationTimeDao.findAll()).willReturn(times);

        List<ReservationTime> result = reservationTimeService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void save_정상_시간_저장() {
        LocalTime startAt = LocalTime.of(23, 0);
        ReservationTime saved = new ReservationTime(14L, startAt);
        given(reservationTimeDao.existsByStartAt(startAt)).willReturn(false);
        given(reservationTimeDao.save(startAt)).willReturn(saved);

        ReservationTime result = reservationTimeService.save(startAt);

        assertThat(result.getId()).isEqualTo(14L);
        assertThat(result.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void save_이미_존재하는_시간이면_예외() {
        LocalTime startAt = LocalTime.of(10, 0);
        given(reservationTimeDao.existsByStartAt(startAt)).willReturn(true);

        assertThatThrownBy(() -> reservationTimeService.save(startAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 예약 시간입니다.");
    }

    @Test
    void delete_정상_삭제() {
        given(reservationDao.existsByTimeId(1L)).willReturn(false);

        reservationTimeService.delete(1L);

        then(reservationTimeDao).should().delete(1L);
    }

    @Test
    void delete_예약에_사용중인_시간이면_예외() {
        given(reservationDao.existsByTimeId(1L)).willReturn(true);

        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약에 사용 중인 시간은 삭제할 수 없습니다.");
    }
}
