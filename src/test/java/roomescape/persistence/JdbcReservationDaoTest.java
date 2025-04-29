package roomescape.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcReservationDaoTest {

    private static final DataSource TEST_DATASOURCE = DataSourceBuilder.create()
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:database-test")
            .username("sa")
            .build();


    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(TEST_DATASOURCE);
    private final JdbcReservationDao reservationDao = new JdbcReservationDao(jdbcTemplate);

    @BeforeEach
    void setUp() {
        DatabaseInitializationSettings settings = new DatabaseInitializationSettings();
        settings.setSchemaLocations(List.of("classpath:schema.sql"));
        SqlDataSourceScriptDatabaseInitializer sqlDataSourceScriptDatabaseInitializer =
                new SqlDataSourceScriptDatabaseInitializer(TEST_DATASOURCE, settings);
        sqlDataSourceScriptDatabaseInitializer.initializeDatabase();
    }

    @Test
    void 전체_예약을_조회할_수_있다() {
        //given
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('12:00')");
        jdbcTemplate.update("INSERT INTO reservation(name, date, time_id) VALUES ('test1', '2025-04-21', 1)");
        jdbcTemplate.update("INSERT INTO reservation(name, date, time_id) VALUES ('test2', '2025-04-22', 1)");

        //when
        List<Reservation> reservations = reservationDao.findAll();

        //then
        assertThat(reservations).isEqualTo(List.of(
                new Reservation(1L, "test1", LocalDate.of(2025, 4, 21), new ReservationTime(1L, LocalTime.of(12, 0))),
                new Reservation(2L, "test2", LocalDate.of(2025, 4, 22), new ReservationTime(1L, LocalTime.of(12, 0)))
        ));
    }

    @Test
    void id값으로_예약을_찾을_수_있다() {
        //given
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('12:00')");
        jdbcTemplate.update("INSERT INTO reservation(name, date, time_id) VALUES ('test1', '2025-04-21', 1)");

        //when
        Optional<Reservation> reservation = reservationDao.findById(1L);

        //then
        assertThat(reservation).hasValue(
                new Reservation(1L, "test1", LocalDate.of(2025, 4, 21),
                        new ReservationTime(1L, LocalTime.of(12, 0))));
    }

    @Test
    void id값에_해당하는_예약이_없다면_Optional_empty를_반환한다() {
        //when
        Optional<Reservation> reservation = reservationDao.findById(1L);

        //then
        assertThat(reservation).isEmpty();
    }

    @Test
    void 예약을_생성할_수_있다() {
        //given
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('12:00')");
        Reservation reservation = new Reservation("test", LocalDateTime.of(2025, 3, 3, 1, 1), LocalDate.of(2025, 4, 21),
                new ReservationTime(1L, LocalTime.of(12, 0)));

        //when
        Long createdId = reservationDao.create(reservation);

        //then
        assertThat(reservationDao.findById(createdId))
                .hasValue(new Reservation(1L, "test", LocalDate.of(2025, 4, 21),
                        new ReservationTime(1L, LocalTime.of(12, 0))));
    }

    @Test
    void 예약을_삭제할_수_있다() {
        //given
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('12:00')");
        jdbcTemplate.update("INSERT INTO reservation(name, date, time_id) VALUES ('test1', '2025-04-21', 1)");

        //when
        reservationDao.deleteById(1L);

        //then
        assertThat(reservationDao.findById(1L)).isEmpty();
    }
}
