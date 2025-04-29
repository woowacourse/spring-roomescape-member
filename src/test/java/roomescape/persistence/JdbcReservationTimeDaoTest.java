package roomescape.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;

class JdbcReservationTimeDaoTest {

    private static final DataSource TEST_DATASOURCE = DataSourceBuilder.create()
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:database-test")
            .username("sa")
            .build();


    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(TEST_DATASOURCE);
    private final JdbcReservationTimeDao reservationTimeDao = new JdbcReservationTimeDao(jdbcTemplate);


    @BeforeEach
    void setUp() {
        DatabaseInitializationSettings settings = new DatabaseInitializationSettings();
        settings.setSchemaLocations(List.of("classpath:schema.sql"));
        SqlDataSourceScriptDatabaseInitializer sqlDataSourceScriptDatabaseInitializer =
                new SqlDataSourceScriptDatabaseInitializer(TEST_DATASOURCE, settings);
        sqlDataSourceScriptDatabaseInitializer.initializeDatabase();
    }

    @Test
    void 예약_시간을_저장할_수_있다() {
        //when
        Long createdId = reservationTimeDao.create(new ReservationTime(LocalTime.of(11, 1)));

        //then
        assertThat(reservationTimeDao.findById(createdId))
                .hasValue(new ReservationTime(1L, LocalTime.of(11, 1)));
    }

    @Test
    void id로_예약_시간을_조회할_수_있다() {
        //given
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('11:00')");

        //when
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(1L);

        //then
        assertThat(reservationTime).hasValue(new ReservationTime(1L, LocalTime.of(11, 0)));
    }

    @Test
    void id값이_없다면_빈_Optional_값이_반환된다() {
        //when
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(1L);

        //then
        assertThat(reservationTime).isEmpty();
    }

    @Test
    void 전체_예약_시간을_조회할_수_있다() {
        //given
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('11:00')");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('11:01')");

        //when
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();

        //then
        assertThat(reservationTimes).isEqualTo(List.of(
                new ReservationTime(1L, LocalTime.of(11, 0)),
                new ReservationTime(2L, LocalTime.of(11, 1))
        ));
    }

    @Test
    void id값으로_예약_시간을_삭제한다() {
        //given
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('11:00')");

        //when
        reservationTimeDao.deleteById(1L);

        //then
        assertThat(reservationTimeDao.findById(1L)).isEmpty();
    }
}