package roomescape.dao.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.domain.reservationtime.ReservationTime;

public class JdbcReservationTimeDaoTest {

    private DataSource datasource;
    private JdbcTemplate jdbcTemplate;
    private JdbcReservationTimeDaoImpl reservationTimeDao;

    @BeforeEach
    void init() {
        datasource = new EmbeddedDatabaseBuilder()
                .setName("testdb-" + UUID.randomUUID())
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(datasource);
        reservationTimeDao = new JdbcReservationTimeDaoImpl(jdbcTemplate);
    }

    @DisplayName("시간이 주어졌을 때, db에 저장해야하고, id값을 설정해야 한다.")
    @Test
    void given_time_then_save_db_and_set_id() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));

        //when
        reservationTimeDao.saveReservationTime(reservationTime);

        //then
        assertThat(reservationTime.getId()).isEqualTo(1L);
        assertThat(reservationTimeDao.findAllReservationTimes().size()).isEqualTo(1);
    }

    @DisplayName("db에 저장된 모든 시간을 가져올 수 있어야 한다.")
    @Test
    void get_all_reservation_times() {
        //given
        ReservationTime reservationTime1 = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime1);

        ReservationTime reservationTime2 = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime2);

        ReservationTime reservationTime3 = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime3);

        // when, then
        assertThat(reservationTimeDao.findAllReservationTimes()).hasSize(3);
        assertThat(reservationTimeDao.findAllReservationTimes()).containsExactlyInAnyOrder(
                reservationTime1, reservationTime2, reservationTime3
        );
    }

    @DisplayName("reservationTimeId가 주어졌을 때, 해당하는 데이터를 삭제해야 한다")
    @Test
    void given_reservation_time_id_then_delete_data() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime);

        //when
        reservationTimeDao.deleteReservationTime(1L);

        //then
        assertThat(reservationTimeDao.findAllReservationTimes().size()).isEqualTo(0);
    }

    @DisplayName("db에 저장되어 있는 id를 통해서 reservationTime 객체를 반환할 수 있어야 한다")
    @Test
    void given_reservation_id_then_return_reservation_time_object() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime);

        //when
        ReservationTime findReservationTimeResult = reservationTimeDao.findById(1L)
                .orElse(null);

        //then
        assertThat(reservationTime).isEqualTo(findReservationTimeResult);
    }
}
