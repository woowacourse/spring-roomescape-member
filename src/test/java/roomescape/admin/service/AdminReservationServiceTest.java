package roomescape.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.doamin.Theme;

@ExtendWith(MockitoExtension.class)
class AdminReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private AdminReservationService adminReservationService;

    @DisplayName("관리자는 전체 예약 내역을 조회할 수 있다.")
    @Test
    void getAllReservations() {
        List<Reservation> reservations = List.of(
                new Reservation(
                        1L,
                        "체셔",
                        LocalDate.of(2026, 5, 10),
                        new ReservationTime(2L, LocalTime.of(10, 0)),
                        new Theme(3L, "공포", "무서운 테마", "https://image.test/theme.png")
                )
        );
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = adminReservationService.getAllReservations();

        assertThat(result).isEqualTo(reservations);
    }
}
