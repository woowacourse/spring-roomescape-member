package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationDAOTest {

    @Autowired
    ReservationDAO reservationDAO;
    @Autowired
    ReservationTimeDAO reservationTimeDAO;

    Reservation reservation;

    @BeforeEach
    void setUp() {
        final ReservationTime savedReservationTime = reservationTimeDAO.insert(new ReservationTime(LocalTime.now()));

        reservation = new Reservation("뽀로로", LocalDate.now(), savedReservationTime);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void insert() {
        final Reservation savedReservation = reservationDAO.insert(reservation);

        assertThat(savedReservation.getName()).isEqualTo("뽀로로");
    }

    @Test
    @DisplayName("전체 예약을 조회한다.")
    void selectAll() {
        reservationDAO.insert(reservation);

        final List<Reservation> reservations = reservationDAO.selectAll();

        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservation() {
        reservationDAO.insert(reservation);

        reservationDAO.deleteById(1L);
        final List<Reservation> reservations = reservationDAO.selectAll();

        assertThat(reservations).hasSize(0);
    }
}
