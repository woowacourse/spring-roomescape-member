package roomescape.time.application.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;
import roomescape.time.domain.ReservationTimeRepository;

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
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));
        final ReservationTimeId id = savedTime.getId();

        // when
        final ReservationTime reservationTime = reservationTimeQueryUseCase.get(id);

        // then
        assertThat(reservationTime.getValue()).isEqualTo(time);
    }

    @Test
    @DisplayName("예약 시간을 전체 조회할 수 있다")
    void getAllReservationTimes() {
        // given
        reservationTimeRepository.save(ReservationTime.withoutId(LocalTime.of(10, 0)));
        reservationTimeRepository.save(ReservationTime.withoutId(LocalTime.of(11, 0)));

        // when
        final List<ReservationTime> times = reservationTimeQueryUseCase.getAll();

        // then
        assertThat(times).hasSize(2);
    }
}
