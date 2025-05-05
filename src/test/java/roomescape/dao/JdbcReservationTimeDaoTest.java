package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.ReservationTime;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest
public class JdbcReservationTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcReservationTimeDaoImpl reservationTimeDao;

    @AfterEach
    void clearResource() {
        String sql = "DELETE FROM reservation_time";
        jdbcTemplate.update(sql);
    }

    @DisplayName("시간이 주어졌을 때, db에 저장해야하고 저장된 id를 반환해야 한다.")
    @Test
    void given_time_then_save_db_and_set_id() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));

        //when
        long savedId = reservationTimeDao.saveReservationTime(reservationTime);

        //then
        assertThat(reservationTimeDao.findById(savedId)).isNotNull();
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
    }

    @DisplayName("reservationTimeId가 주어졌을 때, 해당하는 데이터를 삭제해야 한다")
    @Test
    void given_reservation_time_id_then_delete_data() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedId = reservationTimeDao.saveReservationTime(reservationTime);

        //when
        reservationTimeDao.deleteReservationTime(savedId);

        //then
        assertThat(reservationTimeDao.findAllReservationTimes().size()).isEqualTo(0);
    }

    @DisplayName("db에 저장되어 있는 id를 통해서 reservationTime 객체를 반환할 수 있어야 한다")
    @Test
    void given_reservation_id_then_return_reservation_time_object() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedId = reservationTimeDao.saveReservationTime(reservationTime);
        assertThat(reservationTimeDao.findById(savedId)).isNotNull();
    }
}
