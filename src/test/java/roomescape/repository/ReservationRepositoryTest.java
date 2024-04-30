package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.ClientName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@Sql(value = "/schema.sql", executionPhase = BEFORE_TEST_METHOD)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("모든 예약 정보를 조회한다.")
    @Test
    void findAllTest() {
        // When
        List<Reservation> reservations = reservationRepository.findAll();

        // Then
        assertThat(reservations).hasSize(2);
    }

    @DisplayName("특정 예약 정보를 조회한다.")
    @Test
    void findByIdTest() {
        // Given
        Long reservationId = 1L;

        // When
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        // Then
        assertThat(reservation.isPresent()).isTrue();
    }

    @DisplayName("예약 정보를 저장한다.")
    @Test
    void saveTest() {
        // Given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 10));
        Reservation reservation = new Reservation(new ClientName("브라운"), LocalDate.now().plusDays(10), reservationTime);

        // When
        Reservation savedReservation = reservationRepository.save(reservation);

        // Then
        List<Reservation> reservations = reservationRepository.findAll();
        assertAll(
                () -> assertThat(savedReservation.getId()).isEqualTo(3L),
                () -> assertThat(savedReservation.getClientName().getValue()).isEqualTo(reservation.getClientName().getValue()),
                () -> assertThat(savedReservation.getDate()).isEqualTo(reservation.getDate()),
                () -> assertThat(savedReservation.getTime()).isEqualTo(reservation.getTime()),
                () -> assertThat(reservations).hasSize(3)
        );
    }

    @DisplayName("예약 정보를 삭제한다.")
    @Test
    void deleteByIdTest() {
        // When
        reservationRepository.deleteById(1L);

        // Then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(1);
    }
}
