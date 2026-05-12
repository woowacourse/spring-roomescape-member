package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ActiveProfiles("test")
@Import(ReservationTimeDao.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeDaoTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("imgUrl")
    );

    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"),
            LocalTime.parse(resultSet.getString("start_at"))
    );

    @Test
    void 예약_시간_생성_테스트() {
        Long id = reservationTimeDao.insertReservationTime(LocalTime.of(12, 0));

        ReservationTime actual = reservationTimeDao.findById(id);
        ReservationTime expected = jdbcTemplate.queryForObject("SELECT * FROM reservation_time WHERE id = ?", reservationTimeRowMapper, id);

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getStartAt()).isEqualTo(expected.getStartAt());
    }
}
