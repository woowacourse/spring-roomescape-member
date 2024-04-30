package roomescape.console.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.core.domain.ReservationTime;

class ReservationTimeConsoleRepositoryTest {
    private static ReservationTimeConsoleRepository reservationTimeConsoleRepository;

    @BeforeEach
    void setUp() {
        reservationTimeConsoleRepository = new ReservationTimeConsoleRepository();
    }

    @Test
    @DisplayName("예약 시간을 저장한다.")
    void save() {
        final ReservationTime reservationTime = new ReservationTime("10:10");
        final Long id = reservationTimeConsoleRepository.save(reservationTime);

        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("저장된 모든 예약 시간을 조회한다.")
    void findAll() {
        final ReservationTime reservationTime = new ReservationTime("10:10");
        reservationTimeConsoleRepository.save(reservationTime);

        assertThat(reservationTimeConsoleRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("ID로 예약 시간을 조회한다.")
    void findById() {
        final ReservationTime reservationTime = new ReservationTime("10:10");
        final Long id = reservationTimeConsoleRepository.save(reservationTime);

        assertThat(reservationTimeConsoleRepository.findById(id).getStartAt()).isEqualTo(reservationTime.getStartAt());
    }

    @Test
    @DisplayName("ID로 예약 시간을 삭제한다.")
    void deleteById() {
        final ReservationTime reservationTime = new ReservationTime("10:10");
        final Long id = reservationTimeConsoleRepository.save(reservationTime);

        reservationTimeConsoleRepository.deleteById(id);

        assertThat(reservationTimeConsoleRepository.findAll()).isEmpty();
    }
}