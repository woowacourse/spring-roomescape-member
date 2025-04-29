package roomescape.reservation_time.application.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation_time.application.dto.CreateReservationTimeServiceRequest;
import roomescape.reservation_time.domain.ReservationTime;
import roomescape.reservation_time.domain.ReservationTimeId;
import roomescape.reservation_time.domain.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReservationTimeCommandUseCaseImplTest {

    @Autowired
    private ReservationTimeCommandUseCaseImpl reservationTimeCommandUseCase;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("예약 시간을 생성할 수 있다")
    void createReservationTime() {
        // given
        final CreateReservationTimeServiceRequest request = new CreateReservationTimeServiceRequest(LocalTime.of(12, 30));

        // when
        final ReservationTime reservationTime = reservationTimeCommandUseCase.create(request);

        // then
        assertThat(reservationTime.getValue()).isEqualTo(LocalTime.of(12, 30));
        assertThat(reservationTimeRepository.findById(reservationTime.getId()))
                .isPresent();
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다")
    void deleteReservationTime() {
        // given
        final ReservationTime saved =
                reservationTimeRepository.save(ReservationTime.of(
                        ReservationTimeId.unassigned(), LocalTime.of(14, 0)));
        final ReservationTimeId id = saved.getId();

        // when
        reservationTimeCommandUseCase.delete(id);

        // then
        assertThat(reservationTimeRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제하려 하면 예외가 발생한다")
    void deleteNonExistentReservationTime() {
        // given
        final ReservationTimeId id = ReservationTimeId.from(-1L);

        // when
        // then
        assertThatThrownBy(() -> reservationTimeCommandUseCase.delete(id))
                .isInstanceOf(NoSuchElementException.class);
    }
}
