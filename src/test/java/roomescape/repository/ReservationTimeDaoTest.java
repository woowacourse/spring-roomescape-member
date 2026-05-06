package roomescape.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;
import roomescape.domain.TimeStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    private final RowMapper<ReservationTime> rowMapper = (rs, rowNum) -> {
        if (rs.getString("status").equals(TimeStatus.DELETED.toString())) {
            return new ReservationTime(
                    rs.getLong("id"),
                    rs.getObject("start_at", LocalTime.class),
                    TimeStatus.DELETED
            );
        }

        return ReservationTime.create(
                rs.getLong("id"),
                rs.getObject("start_at", LocalTime.class)
        );
    };

    @Test
    @DisplayName("시간 생성 테스트")
    void createReservationTimeTest() {
        ReservationTime reservationTime = ReservationTime.pending(LocalTime.of(9, 0));
        ReservationTime saved = reservationTimeDao.save(reservationTime);

        ReservationTime reservationTimeFromQuery = jdbcTemplate.queryForObject("SELECT * FROM reservation_time WHERE id = ?", rowMapper, saved.id());

        assertThat(saved.id()).isEqualTo(reservationTimeFromQuery.id());
    }

    @Test
    @DisplayName("시간 삭제 테스트")
    void deleteReservationTimeTest() {
        ReservationTime reservationTime = ReservationTime.pending(LocalTime.of(9, 0));
        ReservationTime saved = reservationTimeDao.save(reservationTime);

        reservationTimeDao.deleteByTimeId(saved.id());
        ReservationTime deletedTime = jdbcTemplate.queryForObject("SELECT * FROM reservation_time WHERE id = ?", rowMapper, saved.id());

        TimeStatus expected = TimeStatus.DELETED;
        TimeStatus actual = deletedTime.status();

        assertThat(actual).isEqualTo(expected);
    }
}