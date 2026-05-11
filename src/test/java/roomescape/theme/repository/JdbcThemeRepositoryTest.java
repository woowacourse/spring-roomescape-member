package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.theme.Theme;

@JdbcTest
@Import(JdbcThemeRepository.class)
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcThemeRepository themeRepository;

    @Test
    void 인기_테마_조회() {
        LocalDate start = LocalDate.of(2026, 05, 10);
        LocalDate end = LocalDate.of(2026, 05, 17);
        int limit = 10;

        List<Theme> themes = themeRepository.findPopularThemes(start, end, limit);

        assertThat(themes).hasSize(5);
        assertThat(themes.getFirst().getId()).isEqualTo(1L);
    }
}
