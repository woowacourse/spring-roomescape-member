package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcReservationTimeRepositoryTest {

    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("test");
        dataSource.setPassword("");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation");
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time");
        jdbcTemplate.execute("DROP TABLE IF EXISTS theme");

        jdbcTemplate.execute("""
                CREATE TABLE reservation_time (
                    id       BIGINT NOT NULL AUTO_INCREMENT,
                    start_at TIME   NOT NULL,
                    PRIMARY KEY (id),
                    UNIQUE (start_at)
                )
                """);

        reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("예약 시간을 저장하고 조회한다.")
    void saveAndFindById() {
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        Optional<ReservationTime> found = reservationTimeRepository.findById(reservationTime.getId());

        assertThat(found).isPresent();
        ReservationTime time = found.get();
        assertThat(time.getId()).isEqualTo(reservationTime.getId());
        assertThat(time.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void findAll() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes).hasSize(1);
        assertThat(reservationTimes.getFirst().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("예약 시간 존재 여부를 조회한다.")
    void existsByStartAt() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        boolean exists = reservationTimeRepository.existsByStartAt(LocalTime.of(10, 0));

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteById() {
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        reservationTimeRepository.deleteById(reservationTime.getId());

        assertThat(reservationTimeRepository.findAll()).isEmpty();
    }
}
