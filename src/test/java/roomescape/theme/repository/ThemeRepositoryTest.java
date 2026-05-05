package roomescape.theme.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.model.Theme;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 테마를_데이터베이스에_성공적으로_저장하고_생성된_ID를_반환한다() {
        Theme theme = new Theme("테마", "설명", "경로");

        Long savedId = themeRepository.create(theme);

        assertThat(savedId).isNotNull();

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme WHERE id = ?", Integer.class, savedId);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void 테마를_데이터베이스에서_정상적으로_삭제한다() {
        Theme theme = new Theme("테마", "삭제", "경로");
        Long savedId = themeRepository.create(theme);

        themeRepository.delete(savedId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme WHERE id = ?", Integer.class, savedId);
        assertThat(count).isEqualTo(0);
    }
}
