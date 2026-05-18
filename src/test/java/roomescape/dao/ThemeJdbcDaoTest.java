package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.jdbc.ThemeJdbcDao;
import roomescape.domain.Theme;
import roomescape.domain.vo.Name;

@JdbcTest
@Import(ThemeJdbcDao.class)
@ActiveProfiles("test")
class ThemeJdbcDaoTest {

    @Autowired
    private ThemeDao themeDao;

    @Test
    @DisplayName("테마를 수정하면 변경된 값이 저장된다")
    void update() {
        Theme saved = themeDao.insert(new Theme(new Name("방탈출"), "http://url", "설명"));
        Theme toUpdate = new Theme(saved.getId(), new Name("새테마"), "http://new-url", "새설명");

        themeDao.update(toUpdate);

        Theme updated = themeDao.findById(saved.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo(new Name("새테마"));
        assertThat(updated.getThumbnailUrl()).isEqualTo("http://new-url");
        assertThat(updated.getDescription()).isEqualTo("새설명");
    }
}
