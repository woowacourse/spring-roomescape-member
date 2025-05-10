package roomescape.dao.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dao.reservation.JdbcReservationDao;
import roomescape.dao.reservationTime.JdbcReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import({JdbcReservationTimeDao.class, JdbcReservationDao.class, JdbcThemeDao.class})
@Sql({"/schema.sql", "/theme-data.sql"})
class JdbcThemeDaoTest {

    @Autowired
    private JdbcReservationTimeDao jdbcReservationTimeDao;
    @Autowired
    private JdbcThemeDao jdbcThemeDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("데이터베이스에서 전체 테마를 조회한다.")
    @Test
    void findAll() {

        // when
        final List<Theme> themes = jdbcThemeDao.findAll();

        // then
        assertThat(themes.size()).isEqualTo(3);
    }

    @DisplayName("데이터베이스에 테마를 저장한다.")
    @Test
    void createTest() {

        // given
        final Theme theme = new Theme("test1", "test1", "test1");

        // when
        final Theme savedTheme = jdbcThemeDao.create(theme);

        // then
        assertAll(
                () -> assertThat(savedTheme.getName()).isEqualTo("test1"),
                () -> assertThat(savedTheme.getDescription()).isEqualTo("test1"),
                () -> assertThat(savedTheme.getThumbnail()).isEqualTo("test1")
        );
    }

    @DisplayName("데이터베이스에 특정 테마가 예약에 사용되지 않는 경우 삭제한다.")
    @Test
    void deleteTest() {

        // then
        assertThat(jdbcThemeDao.deleteIfNoReservation(3L)).isTrue();
    }

    @DisplayName("데이터베이스에 특정 테마가 예약에 사용되는 경우 삭제하지 않는다.")
    @Test
    void deleteIfReservationExist() {

        // then
        assertThat(jdbcThemeDao.deleteIfNoReservation(1L)).isFalse();
    }

    @DisplayName("id로 테마를 찾는다.")
    @Test
    void findByIdTest() {

        // given
        final Theme theme = new Theme("test1", "test1", "test1");
        final Theme savedTheme = jdbcThemeDao.create(theme);

        // when
        final Optional<Theme> optionalTheme = jdbcThemeDao.findById(savedTheme.getId());

        // then
        assertAll(
                () -> assertThat(optionalTheme.isPresent()).isTrue(),
                () -> assertThat(optionalTheme.get()).isEqualTo(savedTheme)
        );
    }

    @DisplayName("최근 일주일의 인기 테마를 가져온다.")
    @Test
    void findPopularThemesInRecentSevenDaysTest() {

        // given
        final LocalTime time = LocalTime.of(10, 10);
        final LocalDate beforeOneDay = LocalDate.now().minusDays(1);
        final LocalDate beforeEightDay = LocalDate.now().minusDays(8);
        final Theme popularTheme = new Theme("test", "test", "test");
        final Theme notPopularTheme = new Theme("test2", "test", "test");
        final ReservationTime savedReservationTime = jdbcReservationTimeDao.create(new ReservationTime(time));
        final Theme savedPopularTheme = jdbcThemeDao.create(popularTheme);
        final Theme savedNotPopularTheme = jdbcThemeDao.create(notPopularTheme);
        final String sql = "INSERT INTO reservation (date, time_id, theme_id, member_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, beforeOneDay.toString(), savedReservationTime.getId(), savedPopularTheme.getId(), 1L);
        jdbcTemplate.update(sql, beforeEightDay.toString(), savedReservationTime.getId(), savedNotPopularTheme.getId(),
                1L);

        // when
        final List<Theme> themes = jdbcThemeDao.findPopularThemesInRecentSevenDays(
                LocalDate.now().minusDays(7), beforeOneDay);

        // then
        assertThat(themes.getFirst()).isEqualTo(savedPopularTheme);
    }

    @DisplayName("데이터베이스에 존재할 경우 true를 반환한다.")
    @Test
    void existsByIdReturnTrueTest() {

        // when & then
        assertThat(jdbcThemeDao.existsById(1L)).isTrue();
    }

    @DisplayName("데이터베이스에 존재하지 않을 경우 false를 반환한다.")
    @Test
    void nonExistsByIdReturnFalseTest() {

        // when & then
        assertThat(jdbcThemeDao.existsById(5L)).isFalse();
    }
}
