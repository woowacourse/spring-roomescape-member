package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequestDto;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationServiceTest {

    @Autowired
    private final ReservationService reservationService;

    @Autowired
    public ReservationServiceTest(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Test
    void getAllReservationsTest() {
        List<Reservation> reservations = reservationService.getAllReservations();

        assertThat(reservations.size()).isEqualTo(1);
    }

    @Test
    void insertReservationTest() {
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto("test", "2024-01-01", 1L);
        Reservation reservation = reservationService.insertReservation(reservationRequestDto);

        assertThat(reservation.getId()).isEqualTo(2L);
        assertThat(reservation.getName()).isEqualTo(reservationRequestDto.name());
        assertThat(reservation.getDate()).isEqualTo(reservationRequestDto.date());
    }

    @Test
    void deleteReservationTest() {
        int sizeBeforeDelete = reservationService.getAllReservations().size();
        assertThatCode(() -> reservationService.deleteReservation(1L)).doesNotThrowAnyException();
        assertThat(reservationService.getAllReservations().size()).isEqualTo(sizeBeforeDelete - 1);
    }
}
