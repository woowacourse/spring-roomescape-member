package roomescape.application.london;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.application.ReservationService;
import roomescape.application.ReservationTimeService;
import roomescape.application.dto.ReservationCreationRequest;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.time.ReservationTime;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    private final ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

    @InjectMocks
    private ReservationService reservationService;
    @Spy
    private Clock clock = Clock.fixed(Instant.parse("2024-04-20T00:00:00Z"), ZoneId.of("UTC"));
    @Mock
    private ReservationTimeService reservationTimeService;
    @Mock
    private ReservationRepository reservationRepository;

    @Test
    void 예약을_성공한다() {
        LocalDate date = LocalDate.of(2024, 4, 21);
        when(reservationTimeService.getReservationTime(anyLong())).thenReturn(time);
        when(reservationRepository.existsByReservationDateTime(any(), anyLong())).thenReturn(false);
        when(reservationRepository.save(any())).thenReturn(new Reservation(1L, "prin", date, time));

        ReservationCreationRequest request = new ReservationCreationRequest("prin", date, 1L);
        Reservation reservation = reservationService.reserve(request);

        assertThat(reservation.getName()).isEqualTo("prin");
        assertThat(reservation.getDate()).isEqualTo(date);
        assertThat(reservation.getTime()).isEqualTo(time.getStartAt());
    }

    @Test
    void 최소_1일_전에_예약하지_않으면_예약을_실패한다() {
        when(reservationTimeService.getReservationTime(anyLong())).thenReturn(time);
        LocalDate invalidDate = LocalDate.of(2024, 4, 20);
        ReservationCreationRequest request = new ReservationCreationRequest("liv", invalidDate, 1L);

        assertThatThrownBy(() -> reservationService.reserve(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("예약은 최소 1일 전에 해야합니다.");
    }

    @Test
    void 중복된_예약이_있으면_예약을_실패한다() {
        when(reservationTimeService.getReservationTime(anyLong())).thenReturn(time);
        when(reservationRepository.existsByReservationDateTime(any(), anyLong())).thenReturn(true);
        ReservationCreationRequest request = new ReservationCreationRequest("sudal", LocalDate.of(2024, 4, 21), 1L);

        assertThatThrownBy(() -> reservationService.reserve(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 예약된 날짜, 시간입니다.");
    }

    @Test
    void 예약을_취소한다() {
        LocalDate date = LocalDate.of(2024, 4, 21);
        Reservation reservation = new Reservation(1L, "prin", date, time);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        reservationService.cancel(1L);

        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void 존재하지_않는_예약을_취소하면_예외가_발생한다() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.cancel(1L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 예약입니다.");
    }
}
