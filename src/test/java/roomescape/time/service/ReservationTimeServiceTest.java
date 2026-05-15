package roomescape.time.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.global.exception.policy.ReservationTimeDeletionNotAllowedException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.util.fixture.ReservationFixture;
import roomescape.util.fixture.ReservationTimeFixture;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    ReservationTimeRepository reservationTimeRepository;

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationTimeService reservationTimeService;

    @Nested
    @DisplayName("시간 삭제")
    class delete {

        @Test
        void 남아있는_예약이_존재하지_않는_시간을_삭제한다() {
            // given
            ReservationTime targetTime = ReservationTimeFixture.create(LocalTime.now());
            ReservationTime otherTime = ReservationTimeFixture.create(LocalTime.now().plusHours(1));

            when(reservationTimeRepository.findById(targetTime.getId()))
                    .thenReturn(targetTime);

            when(reservationRepository.findReservationsFrom(any(LocalDate.class)))
                    .thenReturn(List.of(ReservationFixture.createByTime(otherTime)));

            // when & then
            Assertions.assertThatCode(() -> reservationTimeService.deleteReservationTime(targetTime.getId()))
                    .doesNotThrowAnyException();
        }

        @Test
        void 예약이_존재하는_시간은_삭제할_수_없다() {
            // given
            ReservationTime targetTime = ReservationTimeFixture.create(LocalTime.now());

            when(reservationTimeRepository.findById(targetTime.getId()))
                    .thenReturn(targetTime);

            when(reservationRepository.findReservationsFrom(any(LocalDate.class)))
                    .thenReturn(List.of(ReservationFixture.createByTime(targetTime)));

            // when & then
            Assertions.assertThatThrownBy(
                            () -> reservationTimeService.deleteReservationTime(targetTime.getId()))
                    .isInstanceOf(ReservationTimeDeletionNotAllowedException.class);
        }
    }
}
