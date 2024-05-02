package roomescape.reservationtime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.util.DummyDataFixture;

@SpringBootTest
class JdbcReservationTimeRepositoryTest extends DummyDataFixture {

    @Autowired
    private JdbcReservationTimeRepository jdbcReservationTimeRepository;

    @Test
    @DisplayName("ReservationTime 저장한 후 저장한 row의 id값을 반환한다.")
    void save() {
        ReservationTime newReservationTime = new ReservationTime(null, LocalTime.of(10, 00));
        assertThat(jdbcReservationTimeRepository.save(newReservationTime)).isEqualTo(new ReservationTime(4L, LocalTime.of(10, 00)));
    }

    @Test
    @DisplayName("ReservationTime 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        List<ReservationTime> preparedReservationTimes = super.getPreparedReservationTimes();
        assertThat(jdbcReservationTimeRepository.findAll()).isEqualTo(preparedReservationTimes);
    }

    @Test
    @DisplayName("ReservationTime 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        ReservationTime reservationTime = super.getReservationTimeById(1L);
        assertThat(jdbcReservationTimeRepository.findById(1L)).isEqualTo(Optional.of(reservationTime));
    }

    @Test
    @DisplayName("ReservationTime 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(jdbcReservationTimeRepository.findById(10L)).isNotPresent();
    }

    @Test
    @DisplayName("ReservationTime 테이블에 주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        jdbcReservationTimeRepository.deleteById(3L);
        assertThat(jdbcReservationTimeRepository.findById(3L)).isNotPresent();
    }
}
