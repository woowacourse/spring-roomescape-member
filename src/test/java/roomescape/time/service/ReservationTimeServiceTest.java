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
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

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
        void 시간을_삭제한다() {
            // given
            Long timeIdA = 1L;
            Long timeIdB = 2L;

            when(reservationTimeRepository.findById(timeIdA))
                    .thenReturn(new ReservationTime(timeIdA, LocalTime.now()));

            when(reservationRepository.findReservationsFrom(any(LocalDate.class)))
                    .thenReturn(List.of(new Reservation(1L, "userA", LocalDate.now(),
                            new ReservationTime(timeIdB, LocalTime.now()),
                            new Theme(1L, "themeA", "hello", "/image/..."))));

            // when & then
            Assertions.assertThatCode(() -> reservationTimeService.removeRegisteredReservationTime(timeIdA))
                    .doesNotThrowAnyException();
        }

        @Test
        void 예약이_존재하는_시간은_삭제할_수_없다() {
            // given
            Long timeId = 1L;
            LocalDate today = LocalDate.now();
            LocalTime futureTime = LocalTime.now().plusHours(1);

            when(reservationTimeRepository.findById(timeId))
                    .thenReturn(new ReservationTime(timeId, LocalTime.now()));

            when(reservationRepository.findReservationsFrom(any(LocalDate.class)))
                    .thenReturn(List.of(new Reservation(1L, "userA", today,
                            new ReservationTime(timeId, futureTime),
                            new Theme(1L, "themeA", "hello", "/image/..."))));

            // when & then
            Assertions.assertThatThrownBy(() -> reservationTimeService.removeRegisteredReservationTime(timeId))
                    .isInstanceOf(ReservationTimeDeletionNotAllowedException.class);
        }
    }
}
