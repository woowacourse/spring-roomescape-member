package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.theme.domain.Theme;

@JdbcTest
class JDBCThemeRepositoryTest {
    private final LocalDate futureDate = LocalDate.now().plusDays(10);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new JDBCThemeRepository(jdbcTemplate);
    }

    @Test
    void findTop10PopularThemesWithinLastWeek_shouldReturnCorrectly() {
        List<Theme> top10PopularThemesWithinLastWeek = themeRepository.findTop10PopularThemesWithinLastWeek(futureDate);
        for (Theme theme : top10PopularThemesWithinLastWeek) {
            System.out.println(theme.getId());
        }
    }
}