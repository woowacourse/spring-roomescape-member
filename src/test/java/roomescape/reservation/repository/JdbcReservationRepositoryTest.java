package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.fixture.TestFixture;
import roomescape.reservationtime.domain.ReservationTime;

class JdbcReservationRepositoryTest {

    private JdbcTemplate jdbcTemplate;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("/schema.sql", "/test-data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        reservationRepository = new JDBCReservationRepository(jdbcTemplate);

        jdbcTemplate.update("insert into reservation(name, date, time_id, theme_id) VALUES (?,?,?,?)",
                "mint", TestFixture.makeTomorrowMessage(), "1", "1");
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DROP TABLE reservation IF EXISTS");
        jdbcTemplate.update("DROP TABLE reservation_time IF EXISTS");
        jdbcTemplate.update("DROP TABLE theme IF EXISTS");
    }

    @Test
    void findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations.size()).isEqualTo(1);
    }

    @Test
    void save() {
        LocalDate now = LocalDate.now();
        ReservationTime reservationTime = TestFixture.makeReservationTime(1L);
        Reservation reservation = TestFixture.makeReservation(1L, reservationTime.getId());
        reservationRepository.save(reservation);

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations.size()).isEqualTo(2);
    }

    @Test
    void delete() {
        reservationRepository.deleteById(1L);

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).isEmpty();
    }
}
