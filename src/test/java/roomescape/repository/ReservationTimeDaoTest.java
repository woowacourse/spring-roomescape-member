package roomescape.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.ReservationTime;
import roomescape.domain.TimeStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
    }

    private final RowMapper<ReservationTime> rowMapper = (rs, rowNum) -> {
        if (rs.getString("status").equals(TimeStatus.DELETED.toString())) {
            return new ReservationTime(
                    rs.getLong("id"),
                    rs.getObject("start_at", LocalTime.class),
                    TimeStatus.DELETED
            );
        }

        return ReservationTime.of(
                rs.getLong("id"),
                rs.getObject("start_at", LocalTime.class)
        );
    };

    @Test
    @DisplayName("시간 생성 테스트")
    void CreateReservationTimeTest() {
        ReservationTime reservationTime = ReservationTime.pending(LocalTime.of(9, 0));
        ReservationTime saved = reservationTimeDao.save(reservationTime);

        ReservationTime reservationTimeFromQuery = jdbcTemplate.queryForObject("SELECT * FROM reservation_time WHERE id = ?", rowMapper, saved.id());

        assertThat(saved.id()).isEqualTo(reservationTimeFromQuery.id());
    }

    @Test
    @DisplayName("시간 삭제 테스트")
    void DeleteReservationTimeTest() {
        ReservationTime reservationTime = ReservationTime.pending(LocalTime.of(9, 0));
        ReservationTime saved = reservationTimeDao.save(reservationTime);

        reservationTimeDao.deleteByTimeId(saved.id());
        ReservationTime deletedTime = jdbcTemplate.queryForObject("SELECT * FROM reservation_time WHERE id = ?", rowMapper, saved.id());

        TimeStatus expected = TimeStatus.DELETED;
        TimeStatus actual = deletedTime.status();

        assertThat(actual).isEqualTo(expected);
    }
}
