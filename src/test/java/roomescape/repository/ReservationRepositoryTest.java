package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationRepositoryTest {

    private ReservationRepository reservationRepository;
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

        jdbcTemplate.execute("""
                CREATE TABLE reservation_time (
                    id       BIGINT       NOT NULL AUTO_INCREMENT,
                    start_at VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                )
                """);

        jdbcTemplate.execute("""
                CREATE TABLE reservation (
                    id      BIGINT       NOT NULL AUTO_INCREMENT,
                    name    VARCHAR(255) NOT NULL,
                    date    VARCHAR(255) NOT NULL,
                    time_id BIGINT,
                    PRIMARY KEY (id),
                    FOREIGN KEY (time_id) REFERENCES reservation_time (id)
                )
                """);

        reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }


    @Test
    void 예약을_저장하고_조회한다() {
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        Reservation reservation = reservationRepository.save(new Reservation("브라운", LocalDate.of(2023, 8, 5), time));

        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).hasSize(1);

        Reservation found = reservations.getFirst();
        assertThat(found.getId()).isEqualTo(reservation.getId());
        assertThat(found.getName()).isEqualTo("브라운");
        assertThat(found.getDate()).isEqualTo("2023-08-05");
        assertThat(found.getTime().getId()).isEqualTo(time.getId());
        assertThat(found.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 예약을_삭제한다() {
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        Reservation reservation = reservationRepository.save(new Reservation("브라운", LocalDate.of(2023, 8, 5), time));

        reservationRepository.deleteById(reservation.getId());

        assertThat(reservationRepository.findAll()).isEmpty();
    }
}
