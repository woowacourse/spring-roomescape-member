package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.Theme;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

class ThemeJdbcDaoTest extends RepositoryTest {

    @Autowired
    private ThemeDao themeDao;

    private SimpleJdbcInsert jdbcInsert;

    @BeforeEach
    void setUp() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    @DisplayName("테마를 저장한다.")
    void save() {
        // given
        final Theme theme = new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        final Theme savedTheme = themeDao.save(theme);

        // then
        assertThat(savedTheme.getId()).isNotNull();
    }

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void findAll() {
        // given
        final String insertSql = "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertSql, WOOTECO_THEME_NAME, WOOTECO_THEME_DESCRIPTION, THEME_THUMBNAIL);

        // when
        final List<Theme> themes = themeDao.findAll();

        // then
        assertThat(themes).hasSize(1);
    }

    @Test
    @DisplayName("Id로 테마를 조회한다.")
    void findById() {
        // given
        final Theme theme = WOOTECO_THEME();
        final SqlParameterSource params = new BeanPropertySqlParameterSource(theme);
        final Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        // when
        final Optional<Theme> foundTheme = themeDao.findById(id);

        // then
        assertThat(foundTheme).isNotEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 Id로 테마를 조회하면 빈 Optional을 반환한다.")
    void findByNotExistingId() {
        // given
        final Long notExistingId = 1L;

        // when
        final Optional<Theme> foundTheme = themeDao.findById(notExistingId);

        // then
        assertThat(foundTheme).isEmpty();
    }

    @Test
    @DisplayName("Id로 테마를 삭제한다.")
    void deleteById() {
        // given
        final Theme theme = WOOTECO_THEME();
        final SqlParameterSource params = new BeanPropertySqlParameterSource(theme);
        final Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        // when
        themeDao.deleteById(id);

        // then
        final Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme where id = ?", Integer.class, id);
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("최근 일주일을 기준으로 예약이 많은 순으로 테마 10개를 조회한다.")
    void findAllOrderByReservationCountInLastWeek() {
        // given
        final String insertTimeSql = "INSERT INTO reservation_time (start_at) VALUES (?), (?)";
        jdbcTemplate.update(insertTimeSql,
                Time.valueOf(LocalTime.parse(MIA_RESERVATION_TIME)),
                Time.valueOf(LocalTime.parse(TOMMY_RESERVATION_TIME)));
        final String insertThemeSql = "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?), (?, ?, ?)";
        jdbcTemplate.update(insertThemeSql,
                WOOTECO_THEME_NAME, WOOTECO_THEME_DESCRIPTION, THEME_THUMBNAIL,
                HORROR_THEME_NAME, HORROR_THEME_DESCRIPTION, THEME_THUMBNAIL);
        final String insertReservationSql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?), (?, ?, ?, ?), (?, ?, ?, ?)";
        jdbcTemplate.update(insertReservationSql,
                USER_MIA, MIA_RESERVATION_DATE, 1L, 1L,
                USER_TOMMY, TOMMY_RESERVATION_DATE, 2L, 1L,
                "냥", "2030-05-03", 1L, 2L);

        // when
        final List<Theme> allOrderByReservationCountInLastWeek = themeDao.findAllOrderByReservationCountInLastWeek();

        // then
        assertThat(allOrderByReservationCountInLastWeek).extracting(Theme::getName)
                .containsExactly(WOOTECO_THEME_NAME, HORROR_THEME_NAME);
    }
}
