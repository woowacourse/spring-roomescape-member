package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationDaoTest {

    private JdbcTemplate jdbcTemplate;
    private ReservationDao reservationDao;

    @BeforeEach
    void setup() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:reservation_dao_test;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation");

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);

        this.reservationDao = new ReservationDao(jdbcTemplate);
    }

    @Test
    void 예약_추가_테스트() {
        // given
        ReservationTime time = findTimeByStartAt("15:40");
        Reservation reservation = new Reservation(null, "브라운", "2023-08-05", time);

        // when
        Long id = reservationDao.insert(reservation);

        // then
        List<Reservation> reservations = reservationDao.findAll();
        Reservation savedReservation = reservationDao.findBy(id);
        assertAll(
                () -> assertThat(id).isNotNull(),
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(savedReservation.getName()).isEqualTo(reservation.getName()),
                () -> assertThat(savedReservation.getDate()).isEqualTo(reservation.getDate()),
                () -> assertThat(savedReservation.getTime().getStartAt()).isEqualTo(reservation.getTime().getStartAt()));
    }

    @Test
    void 예약_삭제_테스트() {
        // given
        ReservationTime time1 = findTimeByStartAt("15:40");
        ReservationTime time2 = findTimeByStartAt("12:00");
        Reservation reservation1 = new Reservation(null, "브라운", "2023-08-05", time1);
        Reservation reservation2 = new Reservation(null, "구구", "2023-08-06", time2);
        Long id1 = reservationDao.insert(reservation1);
        Long id2 = reservationDao.insert(reservation2);

        // when
        int deletedCount = reservationDao.delete(id1);

        // then
        List<Reservation> reservations = reservationDao.findAll();
        assertAll(
                () -> assertThat(deletedCount).isEqualTo(1),
                () -> assertThat(reservations).hasSize(1),
                () -> assertThatThrownBy(() -> reservationDao.findBy(id1))
                        .isInstanceOf(EmptyResultDataAccessException.class));
    }

    private ReservationTime findTimeByStartAt(String startAt) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE start_at = ?;";
        return jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) -> {
                    ReservationTime reservationTime = new ReservationTime(
                            resultSet.getLong("id"),
                            resultSet.getString("start_at"));
                    return reservationTime;
                }, startAt);
    }
}
