package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dao.ReservationTimeDAO;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationTimeDAO reservationTimeDAO;

    @BeforeEach
    void setUp() {
        reservationTimeDAO.insert(new ReservationTime(LocalTime.now()));
    }

    @Test
    @DisplayName("예약을 저장할 수 있다.")
    void save() {
        final Reservation savedReservation = reservationService.save(new ReservationRequest("뽀로로", LocalDate.now(), 1L));

        assertThat(savedReservation).isNotNull();
    }

    @Test
    @DisplayName("전체 예약을 조회할 수 있다.")
    void findAll() {
        reservationService.save(new ReservationRequest("뽀로로", LocalDate.now(), 1L));

        final List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("예약을 삭제할 수 있다.")
    void delete() {
        reservationService.save(new ReservationRequest("뽀로로", LocalDate.now(), 1L));

        reservationService.delete(1L);
        final List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations).hasSize(0);
    }
}
