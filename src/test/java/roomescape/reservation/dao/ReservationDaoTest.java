package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;

@JdbcTest
@Import(ReservationDao.class)
public class ReservationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationDao reservationDao;

    @Test
    void 이름으로_예약_조회_테스트() {
        // given
        String name = "도우너";

        // when
        List<Reservation> expected = reservationDao.findByName(name);

        // then
        assertThat(name).isEqualTo(expected.getFirst().getName());
    }

    @Test
    void 예약_생성_테스트() {
        Reservation reservation = new Reservation(
                "초록",
                LocalDate.parse("2026-05-05"),
                new ReservationTime(6L, LocalTime.parse("15:00")),
                1L);
        Reservation saved = reservationDao.insert(reservation);

        String sql = "SELECT name FROM reservation WHERE id = ?";
        String actualName = jdbcTemplate.queryForObject(sql, String.class, saved.getId());

        assertThat(actualName).isEqualTo("초록");
    }

    @Test
    void 본인_예약_삭제_성공() {
        String name = "도우너";
        int deleteCount = reservationDao.delete(2L, name);
        assertThat(deleteCount).isEqualTo(1);
    }

    @Test
    void 본인_예약_변경_성공() {
        int updateCount = reservationDao.update(2L, "도우너", LocalDate.parse("2025-05-14"), 7L);

        assertThat(updateCount).isEqualTo(1);
    }

    @Test
    void 본인_예약_삭제_실패() {
        String name = "도우너";
        int deleteCount = reservationDao.delete(1L, name);
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    void 해당_예약_시간에_대한_예약이_존재하는지_테스트() {
        Boolean result = reservationDao.existsByTimeId(1L);
        assertThat(result).isTrue();
    }

    @Test
    void 해당_예약_시간에_대한_예약이_존재하는지_않는지_테스트() {
        Boolean result = reservationDao.existsByTimeId(6L);
        assertThat(result).isFalse();
    }
}
