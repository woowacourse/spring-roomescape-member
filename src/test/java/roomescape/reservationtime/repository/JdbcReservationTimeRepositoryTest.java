package roomescape.reservationtime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.fixture.Fixture;
import roomescape.reservationtime.model.ReservationTime;

@JdbcTest
@Sql(scripts = "/delete-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private JdbcReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("ReservationTime 을 저장한다.")
    void save() {
        ReservationTime saved = reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);

        assertThat(saved)
                .isEqualTo(new ReservationTime(1L, LocalTime.of(10, 10)));
    }

    @Test
    @DisplayName("ReservationTime 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_2);

        assertThat(reservationTimeRepository.findAll())
                .containsExactly(
                        Fixture.RESERVATION_TIME_1,
                        Fixture.RESERVATION_TIME_2);
    }

    @Test
    @DisplayName("ReservationTime 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);

        assertThat(reservationTimeRepository.findById(1L))
                .isEqualTo(Optional.of(Fixture.RESERVATION_TIME_1));
    }

    @Test
    @DisplayName("ReservationTime 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(reservationTimeRepository.findById(99999L))
                .isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("ReservationTime 테이블에 주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        reservationTimeRepository.deleteById(1L);

        assertThat(reservationTimeRepository.findById(1L))
                .isNotPresent();
    }
}
