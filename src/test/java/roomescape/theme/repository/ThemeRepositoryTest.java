package roomescape.theme.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.model.Theme;

import java.time.LocalTime;
import java.util.List;

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
        Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));

        Long savedId = themeRepository.create(theme);

        assertThat(savedId).isNotNull();

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme WHERE id = ?", Integer.class, savedId);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void 테마를_데이터베이스에서_정상적으로_삭제한다() {
        Theme theme = new Theme("테마", "삭제", "경로", LocalTime.of(2, 0));
        Long savedId = themeRepository.create(theme);

        themeRepository.delete(savedId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme WHERE id = ?", Integer.class, savedId);
        assertThat(count).isEqualTo(0);
    }

    @Test
    void 모든_테마_정보를_정상적으로_조회한다() {
        Theme theme1 = new Theme(1L, "테마1", "설명1", "경로1", LocalTime.of(2, 0));
        Theme theme2 = new Theme(2L, "테마2", "설명2", "경로2", LocalTime.of(2, 0));

        themeRepository.create(theme1);
        themeRepository.create(theme2);

        List<Theme> themes = themeRepository.findAll();

        assertThat(themes).isNotNull();
        assertThat(themes.get(0).getName()).isEqualTo("테마1");
        assertThat(themes.get(1).getName()).isEqualTo("테마2");
        assertThat(themes.get(0).getDescription()).isEqualTo("설명1");
        assertThat(themes.get(1).getDescription()).isEqualTo("설명2");
        assertThat(themes.get(0).getImageUrl()).isEqualTo("경로1");
        assertThat(themes.get(1).getImageUrl()).isEqualTo("경로2");
        assertThat(themes.get(0).getRequiredTime()).isEqualTo(LocalTime.of(2, 0));
        assertThat(themes.get(1).getRequiredTime()).isEqualTo(LocalTime.of(2, 0));
    }
}
