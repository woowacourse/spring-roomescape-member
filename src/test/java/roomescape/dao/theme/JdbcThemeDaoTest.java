package roomescape.dao.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.dao.reservation.JdbcReservationDao;
import roomescape.dao.reservationTime.JdbcReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import({JdbcReservationTimeDao.class, JdbcReservationDao.class, JdbcThemeDao.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Sql(scripts = "/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
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

        // given
        Theme theme1 = Theme.create("test1", "test1", "test1");
        Theme theme2 = Theme.create("test1", "test1", "test1");
        Theme savedTheme1 = jdbcThemeDao.create(theme1);
        Theme savedTheme2 = jdbcThemeDao.create(theme2);

        // when
        List<Theme> themes = jdbcThemeDao.findAll();

        // then
        assertThat(themes.size()).isEqualTo(2);
    }

    @DisplayName("데이터베이스에 테마를 저장한다.")
    @Test
    void createTest() {

        // given
        Theme theme = Theme.create("test1", "test1", "test1");

        // when
        Theme savedTheme = jdbcThemeDao.create(theme);

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

        // given
        Theme theme = Theme.create("test1", "test1", "test1");
        Theme savedTheme = jdbcThemeDao.create(theme);

        // when & then
        assertThatCode(() -> jdbcThemeDao.delete(savedTheme))
                .doesNotThrowAnyException();
    }

    @DisplayName("id로 테마를 찾는다.")
    @Test
    void findByIdTest() {

        // given
        Theme theme = Theme.create("test1", "test1", "test1");
        Theme savedTheme = jdbcThemeDao.create(theme);

        // when
        Optional<Theme> optionalTheme = jdbcThemeDao.findById(savedTheme.getId());

        // then
        assertAll(
                () -> assertThat(optionalTheme.isPresent()).isTrue(),
                () -> assertThat(optionalTheme.get()).isEqualTo(savedTheme)
        );
    }

    @DisplayName("최근 7일간 가장 인기있었던 테마 상위 10개를 검색한다.")
    @Test
    void findPopularThemesInRecentSevenDaysTest() {

        // given
        LocalTime time = LocalTime.of(10, 10);
        LocalDate beforeOneDay = LocalDate.now().minusDays(1);
        LocalDate beforeEightDay = LocalDate.now().minusDays(8);
        Theme popularTheme = Theme.create("test", "test", "test");
        Theme notPopularTheme = Theme.create("test2", "test", "test");
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(ReservationTime.create(time));
        Theme savedPopularTheme = jdbcThemeDao.create(popularTheme);
        Theme savedNotPopularTheme = jdbcThemeDao.create(notPopularTheme);
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('test', ?, ?, ?)";
        jdbcTemplate.update(sql, beforeOneDay.toString(), savedReservationTime.getId(), savedPopularTheme.getId());
        jdbcTemplate.update(sql, beforeEightDay.toString(), savedReservationTime.getId(), savedNotPopularTheme.getId());

        // when
        List<Theme> themes = jdbcThemeDao.findPopularThemesInRecentSevenDays(
                LocalDate.now().minusDays(7), beforeOneDay);

        // then
        assertThat(themes.getFirst()).isEqualTo(savedPopularTheme);
    }
}
