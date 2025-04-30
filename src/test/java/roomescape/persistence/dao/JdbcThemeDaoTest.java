package roomescape.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Theme;
import roomescape.persistence.entity.PlayTimeEntity;
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
}
