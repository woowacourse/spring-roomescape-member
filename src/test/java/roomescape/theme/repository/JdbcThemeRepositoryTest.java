package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import roomescape.theme.domain.Theme;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcThemeRepository jdbcThemeRepository;

    @Test
    void findAll() {
        assertThat(jdbcThemeRepository.findAll()).isEmpty();

        jdbcThemeRepository.save(new Theme("이름", "설명", "https://img.test/a.png"));

        List<Theme> themes = jdbcThemeRepository.findAll();
        assertThat(themes).hasSize(1);
        assertThat(themes.get(0).getId()).isEqualTo(1L);
        assertThat(themes.get(0).getName()).isEqualTo("이름");
        assertThat(themes.get(0).getDescription()).isEqualTo("설명");
        assertThat(themes.get(0).getImageUrl()).isEqualTo("https://img.test/a.png");
    }

    @Test
    void save() {
        Theme saved = jdbcThemeRepository.save(new Theme("테마", "내용", "https://img.test/b.png"));

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getName()).isEqualTo("테마");
        assertThat(saved.getDescription()).isEqualTo("내용");
        assertThat(saved.getImageUrl()).isEqualTo("https://img.test/b.png");
    }

    @Test
    void deleteById() {
        jdbcThemeRepository.save(new Theme("x", "y", "https://img.test/c.png"));

        assertThat(jdbcThemeRepository.deleteById(1L)).isTrue();
        assertThat(jdbcThemeRepository.findAll()).isEmpty();

        assertThat(jdbcThemeRepository.deleteById(1L)).isFalse();
    }
}
