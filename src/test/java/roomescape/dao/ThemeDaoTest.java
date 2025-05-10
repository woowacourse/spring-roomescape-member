package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Theme;

@JdbcTest
public class ThemeDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    ThemeDao themeDao;
    Theme savedTheme;

    @BeforeEach
    void setUp() {
        Theme theme = new Theme("탈출하고싶다", "탈출하는내용", "abc.jpg");
        themeDao = new ThemeDao(jdbcTemplate);
        savedTheme = themeDao.save(theme);
    }

    @Test
    @DisplayName("id로 Theme 을 조회 할 수 있다")
    void select_with_id() {
        Optional<Theme> theme = themeDao.findById(savedTheme.getId());
        assertThat(theme.get()).isEqualTo(savedTheme);
    }

    @Test
    @DisplayName("Theme을 저장한다")
    void save() {
        String themeName = "우아한테크코스";
        Theme theme = new Theme(
            themeName,
            "재밌다",
            "abc.jpg"
        );
        Theme savedTheme = themeDao.save(theme);
        assertThat(savedTheme.getName()).isEqualTo(themeName);
    }

    @Test
    @DisplayName("저장된 Theme 전체를 불러온다")
    void select_all() {
        assertThat(themeDao.findAll()).contains(savedTheme);
    }

    @Test
    @DisplayName("id로 해당 entity를 삭제한다")
    void delete_with_id() {
        Assertions.assertDoesNotThrow(() -> themeDao.deleteById(savedTheme.getId()));
    }
}
