package roomescape.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TimeDaoTest {
    private static final RowMapper<ReservationTime> timeRowMapper = (rs, rowNum) ->
            new ReservationTime(rs.getLong("id")
                    , rs.getTime("start_at").toLocalTime());

    @Autowired
    private TimeDao timeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 예약_시간_조회_테스트() {

        Long themeId = 2L;
        LocalDate date = LocalDate.parse("2026-05-05");
        List<AvailableTime> availableTimes = timeDao.selectByThemeIdAndDate(themeId, date);

        assertThat(availableTimes.size()).isEqualTo(11);
        assertThat(availableTimes.getFirst().getIsAvailable()).isFalse();
    }

    @Test
    void 시간_생성_테스트() {
        ReservationTime time = new ReservationTime(LocalTime.parse("21:00"));
        ReservationTime expected = timeDao.insert(time);

        String sql = "select id, start_at from reservation_time where id = ?";
        ReservationTime actual = jdbcTemplate.query(sql, timeRowMapper, expected.getId()).getFirst();

        assertThat(expected.getId()).isEqualTo(actual.getId());
        assertThat(expected.getStartAt()).isEqualTo(actual.getStartAt());
    }
}
