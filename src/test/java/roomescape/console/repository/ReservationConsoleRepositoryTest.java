package roomescape.console.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.core.domain.Reservation;
import roomescape.core.domain.ReservationTime;

class ReservationConsoleRepositoryTest {
    private static ReservationConsoleRepository reservationConsoleRepository;
    private static ReservationTime reservationTime;

    @BeforeEach
    void setUp() {
        reservationConsoleRepository = new ReservationConsoleRepository();
        reservationTime = new ReservationTime("10:10");

        ReservationTimeConsoleRepository reservationTimeConsoleRepository = new ReservationTimeConsoleRepository();
        final Long id = reservationTimeConsoleRepository.save(reservationTime);
        reservationTime = reservationTimeConsoleRepository.findById(id);
    }

    @Test
    @DisplayName("예약을 저장한다.")
    void save() {
        Reservation reservation = new Reservation("리건", "2023-10-10", reservationTime);
        final Long id = reservationConsoleRepository.save(reservation);

        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("저장된 모든 예약을 조회한다.")
    void findAll() {
        Reservation reservation = new Reservation("리건", "2023-10-10", reservationTime);
        reservationConsoleRepository.save(reservation);

        assertThat(reservationConsoleRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("시간 ID로 예약을 조회한다.")
    void findByTimeId() {
        Reservation reservation = new Reservation("리건", "2023-10-10", reservationTime);
        reservationConsoleRepository.save(reservation);

        assertThat(reservationConsoleRepository.findByTimeId(1L)).hasSize(1);
    }

    @Test
    @DisplayName("ID로 예약을 삭제한다.")
    void deleteById() {
        Reservation reservation = new Reservation("리건", "2023-10-10", reservationTime);
        final Long id = reservationConsoleRepository.save(reservation);

        reservationConsoleRepository.deleteById(id);

        assertThat(reservationConsoleRepository.findAll()).isEmpty();
    }
}
