package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequestDto;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
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
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto("test", LocalDate.now().plusDays(1), 1L, 1L);
        Reservation reservation = reservationService.insertReservation(reservationRequestDto);

        assertThat(reservation.getId()).isEqualTo(2L);
        assertThat(reservation.getName()).isEqualTo(reservationRequestDto.name());
        assertThat(reservation.getDate()).isEqualTo(reservationRequestDto.date().toString());
    }

    @Test
    void deleteReservationTest() {
        int sizeBeforeDelete = reservationService.getAllReservations().size();
        assertThatCode(() -> reservationService.deleteReservation(1L)).doesNotThrowAnyException();
        assertThat(reservationService.getAllReservations().size()).isEqualTo(sizeBeforeDelete - 1);
    }

    @Test
    void invalidDateTimeTest() {
        LocalDate localDate = LocalDate.now().minusDays(2);

        ReservationRequestDto reservationRequestDto = new ReservationRequestDto("test", localDate, 1L, 1L);

        assertThatThrownBy(() -> reservationService.insertReservation(reservationRequestDto))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
