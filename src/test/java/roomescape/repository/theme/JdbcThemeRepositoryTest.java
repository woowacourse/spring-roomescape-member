package roomescape.repository.theme;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Import(JdbcThemeRepository.class)
@JdbcTest
class JdbcThemeRepositoryTest {

    private static final Theme THEME = new Theme(null, "name", "description", "image-url");

    private final ThemeRepository repository;

    @Autowired
    public JdbcThemeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.repository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    void 테마_추가_테스트() {
        // given & when
        Theme saved = repository.createTheme(THEME);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo(THEME.getName());
        assertThat(saved.getDescription()).isEqualTo(THEME.getDescription());
        assertThat(saved.getImageUrl()).isEqualTo(THEME.getImageUrl());
    }

    @Test
    void 기존에_저장된_테마를_id로_찾아서_삭제한다() {
        // given
        Theme saved = repository.createTheme(THEME);

        // when & then
        assertThatCode(() -> repository.deleteById(saved.getId()))
            .doesNotThrowAnyException();
    }
}