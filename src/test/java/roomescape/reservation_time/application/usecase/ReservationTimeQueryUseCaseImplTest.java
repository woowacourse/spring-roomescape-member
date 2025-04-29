package roomescape.reservation_time.application.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation_time.domain.ReservationTime;
import roomescape.reservation_time.domain.ReservationTimeId;
import roomescape.reservation_time.domain.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReservationTimeQueryUseCaseImplTest {

    @Autowired
    private ReservationTimeQueryUseCaseImpl reservationTimeQueryUseCase;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("예약 시간을 조회할 수 있다")
    void getReservationTime() {
        // given
        final LocalTime time = LocalTime.of(10, 0);
        reservationTimeRepository.save(ReservationTime.of(
                ReservationTimeId.unassigned(), time));

        final ReservationTimeId id = reservationTimeQueryUseCase.getAll().getFirst().getId();

        // when
        final ReservationTime reservationTime = reservationTimeQueryUseCase.get(id);

        // then
        assertThat(reservationTime.getValue()).isEqualTo(time);
    }

    @Test
    @DisplayName("예약 시간을 전체 조회할 수 있다")
    void getAllReservationTimes() {
        // given
        reservationTimeRepository.save(ReservationTime.of(
                ReservationTimeId.unassigned(), LocalTime.of(10, 0)));
        reservationTimeRepository.save(ReservationTime.of(
                ReservationTimeId.unassigned(), LocalTime.of(11, 0)));

        // when
        final List<ReservationTime> times = reservationTimeQueryUseCase.getAll();

        // then
        assertThat(times).hasSize(2);
    }
}
