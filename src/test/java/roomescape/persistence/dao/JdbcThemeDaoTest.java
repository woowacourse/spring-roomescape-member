package roomescape.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.Theme;
import roomescape.persistence.entity.ThemeEntity;

@JdbcTest
class JdbcThemeDaoTest {

    private ThemeDao themeDao;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcThemeDaoTest(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        themeDao = new JdbcThemeDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS theme CASCADE");
        jdbcTemplate.execute("""
                CREATE TABLE theme
                (
                    id SERIAL,
                    name        VARCHAR(255) NOT NULL,
                    description VARCHAR(255) NOT NULL,
                    thumbnail VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                );
                """);
    }

    @DisplayName("데이터베이스에 테마를 저장한다.")
    @Test
    void save() {
        // given & when
        final Long id = themeDao.save(new Theme("테마", "소개", "썸네일"));
        final ThemeEntity actual = jdbcTemplate.queryForObject(
                "SELECT id, name, description, thumbnail FROM theme WHERE id = ?",
                ThemeEntity.getDefaultRowMapper(), id
        );

        // then
        assertThat(actual).isEqualTo(new ThemeEntity(1L, "테마", "소개", "썸네일"));
    }

    @DisplayName("데이터베이스에서 테마를 찾는다.")
    @Test
    void find() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마', '소개', '썸네일')");

        // when
        final Optional<Theme> actual = themeDao.find(1L);

        // then
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(Theme.createWithId(1L, "테마", "소개", "썸네일"));
    }

    @DisplayName("해당하는 테마가 없다면 Optional Empty를 반환한다.")
    @Test
    void findNotExistsTheme() {
        // given & when
        final Optional<Theme> actual = themeDao.find(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @DisplayName("데이터베이스에서 모든 테마를 조회한다.")
    @Test
    void findAll() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '소개1', '썸네일1')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마2', '소개2', '썸네일2')");

        // when
        final List<Theme> actual = themeDao.findAll();

        // then
        assertThat(actual).containsExactly(
                Theme.createWithId(1L, "테마1", "소개1", "썸네일1"),
                Theme.createWithId(2L, "테마2", "소개2", "썸네일2")
        );
    }

    @DisplayName("데이터베이스에서 테마를 삭제한다.")
    @Test
    void remove() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마', '소개', '썸네일')");

        // when
        final boolean flag = themeDao.remove(1L);
        final List<Theme> themes = themeDao.findAll();

        // then
        assertAll(
                () -> assertThat(flag).isTrue(),
                () -> assertThat(themes).isEmpty()
        );
    }

    @DisplayName("해당하는 테마가 없다면 false를 반환한다.")
    @Test
    void removeNotExistsTime() {
        // given & when
        final boolean flag = themeDao.remove(1L);

        // then
        assertThat(flag).isFalse();
    }

    @DisplayName("데이터베이스에서 원하는 수만큼의 해당 기간내에 예약이 많은 테마 순으로 조회한다.")
    @Test
    void findPopularThemesBetweenWithLimit() {
        // given
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS reservation
                (
                    id SERIAL,
                    name VARCHAR(255) NOT NULL,
                    date VARCHAR(255) NOT NULL,
                    time_id BIGINT,
                    theme_id BIGINT,
                    PRIMARY KEY (id),
                    FOREIGN KEY (theme_id) REFERENCES theme (id)
                );
                """);
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '소개1', '썸네일1')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마2', '소개2', '썸네일2')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마3', '소개3', '썸네일3')");
        jdbcTemplate.update(
                "INSERT INTO reservation (user_id, date, time_id, theme_id) VALUES (1, '2025-01-01', 1, 2)");

        // when
        final List<Theme> actual = themeDao.findPopularThemesBetweenWithLimit(
                "2024-12-31",
                "2025-01-01",
                2
        );

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).extracting("name")
                        .containsExactly("테마2", "테마1")
        );
    }
}
