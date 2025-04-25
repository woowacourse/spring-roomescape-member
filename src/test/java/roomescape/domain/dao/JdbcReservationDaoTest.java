package roomescape.domain.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.dao.JdbcReservationDaoImpl;
import roomescape.domain.Person;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public class JdbcReservationDaoTest {

    private ReservationTime reservationTime;
    private DataSource datasource;
    private JdbcTemplate jdbcTemplate;
    private JdbcReservationDaoImpl reservationDao;

    @BeforeEach
    void init() {
        reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        datasource = new EmbeddedDatabaseBuilder()
            .setName("testdb-" + UUID.randomUUID())
            .setType(EmbeddedDatabaseType.H2)
            .addScript("schema.sql")
            .build();
        jdbcTemplate = new JdbcTemplate(datasource);
        reservationDao = new JdbcReservationDaoImpl(jdbcTemplate);

        String query = "insert into reservation_time (id, start_at) values (?, ?)";
        jdbcTemplate.update(query, 1L, "10:00");
    }

    @AfterEach
    void remove() {
        String query = "delete from reservation";
        jdbcTemplate.update(query);
    }

    @DisplayName("reservation 객체가 주어졌을 때, db에 저장되며 id값이 부여되어야 한다.")
    @Test
    void given_reservation_then_save_db_and_set_id() {
        //given
        Reservation reservation = new Reservation(new Person("james"), LocalDate.of(2025, 12, 25),
            reservationTime);

        //when
        reservationDao.saveReservation(reservation);

        //then
        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservationDao.findAllReservation().size()).isEqualTo(1);
    }

    @DisplayName("db에 존재하는 모든 reservation을 가져올 수 있어야 한다.")
    @Test
    void get_all_reservation() {
        //given
        Reservation reservation1 = new Reservation(new Person("james"), LocalDate.of(2025, 12, 25),
            reservationTime);
        reservationDao.saveReservation(reservation1);
        Reservation reservation2 = new Reservation(new Person("james"), LocalDate.of(2025, 12, 26),
            reservationTime);
        reservationDao.saveReservation(reservation2);
        Reservation reservation3 = new Reservation(new Person("james"), LocalDate.of(2025, 12, 27),
            reservationTime);
        reservationDao.saveReservation(reservation3);

        //when, then
        assertThat(reservationDao.findAllReservation()).containsExactlyInAnyOrder(reservation1,
            reservation2, reservation3);
        assertThat(reservationDao.findAllReservation().size()).isEqualTo(3);
    }

    @DisplayName("reservationId가 주어졌을 때, 해당하는 데이터를 삭제해야 한다.")
    @Test
    void given_reservation_id_then_delete_data() {
        //given
        Reservation reservation = new Reservation(new Person("james"), LocalDate.of(2025, 12, 25),
            reservationTime);
        reservationDao.saveReservation(reservation);

        //when
        reservationDao.deleteReservation(1L);

        //then
        assertThat(reservationDao.findAllReservation().size()).isEqualTo(0);
    }
}
