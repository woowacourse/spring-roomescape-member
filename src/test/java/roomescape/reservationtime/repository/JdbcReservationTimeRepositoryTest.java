package roomescape.reservationtime.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.reservationtime.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcReservationTimeRepository.class)
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private JdbcReservationTimeRepository jdbcReservationTimeRepository;

    @Test
    @DisplayName("예약 시간을 저장하고 조회한다.")
    void saveAndFindById() {
        ReservationTime reservationTime = jdbcReservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        Optional<ReservationTime> found = jdbcReservationTimeRepository.findById(reservationTime.getId());

        assertThat(found).isPresent();
        ReservationTime time = found.get();
        assertThat(time.getId()).isEqualTo(reservationTime.getId());
        assertThat(time.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void findAll() {
        jdbcReservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        List<ReservationTime> reservationTimes = jdbcReservationTimeRepository.findAll();

        assertThat(reservationTimes).hasSize(1);
        assertThat(reservationTimes.getFirst().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("예약 시간 존재 여부를 조회한다.")
    void existsByStartAt() {
        jdbcReservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        boolean exists = jdbcReservationTimeRepository.existsByStartAt(LocalTime.of(10, 0));

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteById() {
        ReservationTime reservationTime = jdbcReservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        jdbcReservationTimeRepository.deleteById(reservationTime.getId());

        assertThat(jdbcReservationTimeRepository.findAll()).isEmpty();
    }
}
