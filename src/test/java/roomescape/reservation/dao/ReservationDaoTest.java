package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationDaoTest {

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("예약 추가 확인 테스트")
    void insertTest() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(15, 40));
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.of(2023, 8, 5), reservationTime);

        // when
        reservationTimeDao.insert(reservationTime);
        reservationDao.insert(reservation);

        // then
        assertThat(count()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 삭제 확인 테스트")
    void deleteTest() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(15, 40));
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.of(2023, 8, 5), reservationTime);
        reservationTimeDao.insert(reservationTime);
        reservationDao.insert(reservation);

        // when
        reservationDao.delete(1L);

        // then
        assertThat(count()).isEqualTo(0);
    }

    private int count() {
        String sql = "select count(*) from reservation";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}
