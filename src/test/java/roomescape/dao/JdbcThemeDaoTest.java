package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.model.Theme;
import roomescape.infrastructure.dao.JdbcThemeDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.fixture.TestFixture.DEFAULT_THEME;

@JdbcTest
public class JdbcThemeDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    JdbcThemeDao themeDao;

    @BeforeEach
    void setUp() {
        themeDao = new JdbcThemeDao(jdbcTemplate);
    }

    @Test
    void 테마를_저장할_수_있다() {
        // given
        Theme savedTheme = themeDao.save(DEFAULT_THEME);

        // when & then
        assertThat(savedTheme.getName()).isEqualTo(DEFAULT_THEME.getName());
    }

    @Test
    void id로_특정_테마를_조회할_수_있다() {
        // given
        Theme savedTheme = themeDao.save(DEFAULT_THEME);
        Theme foundTheme = themeDao.findById(savedTheme.getId());

        // when & then
        assertAll(
                () -> assertThat(savedTheme.getName()).isEqualTo(foundTheme.getName()),
                () -> assertThat(savedTheme.getDescription()).isEqualTo(foundTheme.getDescription()),
                () -> assertThat(savedTheme.getThumbnail()).isEqualTo(foundTheme.getThumbnail())
        );
    }

    @Test
    void 전체_테마를_조회할_수_있다() {
        // given
        int totalSize = themeDao.findAll().size();
        Theme theme = new Theme(
                "진격의거인",
                "감동적인 이야기",
                "abc.jpg"
        );

        // when
        Theme savedTheme = themeDao.save(theme);

        // then
        assertAll(
                () -> assertThat(themeDao.findAll().size()).isEqualTo(totalSize + 1),
                () -> assertThat(themeDao.findAll()).contains(savedTheme)
        );
    }

    @Test
    void 특정_테마를_삭제할_수_있다() {
        // given
        int totalSize = themeDao.findAll().size();

        // when
        themeDao.deleteById(1L);

        // then
        assertThat(themeDao.findAll().size()).isEqualTo(totalSize - 1);
    }
}
