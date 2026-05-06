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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

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

    @Test
    void 예약_가능한_시간_조회_테스트() {
        jdbcTemplate.update("INSERT INTO theme (name, description, imgUrl) VALUES (?, ?, ?)",
                "이든의 공포 하우스", "이든이 귀신으로 나오는 공포 테마", "image.jpg");
        Long time1Id = reservationTimeDao.insertReservationTime(LocalTime.of(12, 0));
        Long time2Id = reservationTimeDao.insertReservationTime(LocalTime.of(13, 0));

        LocalDate date = LocalDate.of(2026, 5, 6);
        Long themeId = 1L;
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이든", date.toString(), time1Id, themeId);

        Map<ReservationTime, Boolean> actual = reservationTimeDao.findAvailableTimes(date, themeId);

        assertThat(actual).hasSize(2);

        ReservationTime time12 = new ReservationTime(time1Id, LocalTime.of(12, 0));
        ReservationTime time13 = new ReservationTime(time2Id, LocalTime.of(13, 0));

        assertThat(actual.get(time12)).isFalse();
        assertThat(actual.get(time13)).isTrue();
    }
}