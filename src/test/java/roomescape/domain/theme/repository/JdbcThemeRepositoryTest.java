package roomescape.domain.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.theme.Theme;

@JdbcTest
class JdbcThemeRepositoryTest {
    private final ThemeRepository themeRepository;

    @Autowired
    JdbcThemeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    void 테마를_저장한다() {
        Theme theme1 = new Theme("공포", "설명", "썸네일1");
        Theme theme = themeRepository.save(theme1);

        assertThat(theme.id()).isEqualTo(1L);
    }

    @Test
    void 모든_테마를_조회한다() {
        Theme theme1 = new Theme("공포", "설명", "썸네일1");
        Theme theme2 = new Theme("SF", "설명", "썸네일2");
        themeRepository.save(theme1);
        themeRepository.save(theme2);

        List<Theme> themes = themeRepository.findAll();
        assertAll(
                () -> assertThat(themes).hasSize(2),
                () -> assertThat(themes.get(0).name()).isEqualTo("공포"),
                () -> assertThat(themes.get(1).name()).isEqualTo("SF")
        );
    }

    @Test
    void 테마를_삭제한다() {
        Theme theme1 = new Theme("공포", "설명", "썸네일1");
        Theme theme2 = new Theme("SF", "설명", "썸네일2");
        themeRepository.save(theme1);
        themeRepository.save(theme2);

        themeRepository.deleteById(1);

        assertThat(themeRepository.findAll()).hasSize(1);
    }
}
