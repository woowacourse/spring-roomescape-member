package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.reservationtime.dao.ReservationTimeDao;
import roomescape.domain.reservationtime.model.ReservationTime;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest
public class JdbcReservationTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @AfterEach
    void clearResource() {
        String sql1 = "DELETE FROM reservation";
        String sql2 = "DELETE FROM reservation_time";
        String sql3 = "DELETE FROM theme";
        String sql4 = "DELETE FROM member";
        jdbcTemplate.update(sql1);
        jdbcTemplate.update(sql2);
        jdbcTemplate.update(sql3);
        jdbcTemplate.update(sql4);
    }

    @DisplayName("시간이 주어졌을 때, db에 저장해야하고 저장된 id를 반환해야 한다.")
    @Test
    void given_time_then_save_db_and_set_id() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));

        //when
        long savedId = reservationTimeDao.save(reservationTime);

        //then
        assertThat(reservationTimeDao.findById(savedId)).isNotNull();
        assertThat(reservationTimeDao.findAll().size()).isEqualTo(1);
    }

    @DisplayName("db에 저장된 모든 시간을 가져올 수 있어야 한다.")
    @Test
    void get_all_reservation_times() {
        //given
        ReservationTime reservationTime1 = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.save(reservationTime1);

        ReservationTime reservationTime2 = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.save(reservationTime2);

        ReservationTime reservationTime3 = new ReservationTime(LocalTime.of(10, 0));
        reservationTimeDao.save(reservationTime3);

        // when, then
        assertThat(reservationTimeDao.findAll()).hasSize(3);
    }

    @DisplayName("reservationTimeId가 주어졌을 때, 해당하는 데이터를 삭제해야 한다")
    @Test
    void given_reservation_time_id_then_delete_data() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedId = reservationTimeDao.save(reservationTime);

        //when
        reservationTimeDao.delete(savedId);

        //then
        assertThat(reservationTimeDao.findAll().size()).isEqualTo(0);
    }

    @DisplayName("db에 저장되어 있는 id를 통해서 reservationTime 객체를 반환할 수 있어야 한다")
    @Test
    void given_reservation_id_then_return_reservation_time_object() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedId = reservationTimeDao.save(reservationTime);
        assertThat(reservationTimeDao.findById(savedId)).isNotNull();
    }

    @DisplayName("해당 날짜와 테마 아이디에 대해 예약이 이미 되어있는 시간을 가져올 수 있어야 한다.")
    @Test
    void already_reservation_then_true_else_false() {
        initData();
        List<ReservationTime> result = reservationTimeDao.findBookedTimes("2025-04-24", 1L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getStartAt()).isEqualTo("10:00");
    }

    private void initData() {
        String sql1 = "INSERT INTO theme (id, name, description, thumbnail) VALUES (1, '안녕, 자두야', '자두', 'https://jado.com');";
        String sql2 = "INSERT INTO member (id, role, name, email, password) VALUES (1, 'admin', 'jenson', 'a@example.com', 'abc');";
        String sql3 = "INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00');";
        String sql4 = "INSERT INTO reservation_time (id, start_at) VALUES (2, '11:00');";
        String sql5 = "INSERT INTO reservation_time (id, start_at) VALUES (3, '12:00');";
        String sql6 = "INSERT INTO reservation_time (id, start_at) VALUES (4, '13:00');";
        String sql7 = "INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES ('2025-04-24', 1, 1, 1);";
        jdbcTemplate.update(sql1);
        jdbcTemplate.update(sql2);
        jdbcTemplate.update(sql3);
        jdbcTemplate.update(sql4);
        jdbcTemplate.update(sql5);
        jdbcTemplate.update(sql6);
        jdbcTemplate.update(sql7);
    }
}
