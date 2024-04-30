package roomescape.reservation.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservation.model.Reservation;
import roomescape.util.DummyDataFixture;

@SpringBootTest
class JdbcReservationRepositoryTest extends DummyDataFixture {

    @Autowired
    private JdbcReservationRepository jdbcReservationRepository;

    @Test
    @DisplayName("Reservation를 저장한 후 저장한 row의 id값을 반환한다.")
    void save() {
        Reservation newReservation = new Reservation(null, "포비", LocalDate.of(2024, 4, 23), 2L, LocalTime.of(10, 00));
        assertThat(jdbcReservationRepository.save(newReservation)).isEqualTo(3);
    }

    @Test
    @DisplayName("Reservation 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        List<Reservation> preparedReservations = super.getPreparedReservations();
        assertThat(jdbcReservationRepository.findAll()).isEqualTo(preparedReservations);
    }

    @Test
    @DisplayName("Reservation 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        Reservation reservation = super.getReservationById(1L);
        assertThat(jdbcReservationRepository.findById(1L)).isEqualTo(Optional.of(reservation));
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(jdbcReservationRepository.findById(10L)).isNotPresent();
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        jdbcReservationRepository.deleteById(2L);
        assertThat(jdbcReservationRepository.findById(2L)).isNotPresent();
    }
}
