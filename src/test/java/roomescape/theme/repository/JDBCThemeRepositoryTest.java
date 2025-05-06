package roomescape.theme.repository;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.reservation.fixture.TestFixture;
import roomescape.theme.domain.Theme;

class JDBCThemeRepositoryTest {

    private JdbcTemplate jdbcTemplate;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        EmbeddedDatabase database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("/schema.sql", "/test-data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(database);
        themeRepository = new JDBCThemeRepository(jdbcTemplate);
        jdbcTemplate.update(
                "INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('Mint', '2025-05-05', 1, 1)");
        jdbcTemplate.update(
                "INSERT INTO reservation(name, date, time_id, theme_id) VALUES ('Mint', '2025-05-05', 2, 1)");
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DROP TABLE reservation IF EXISTS");
        jdbcTemplate.update("DROP TABLE reservation_time IF EXISTS");
        jdbcTemplate.update("DROP TABLE theme IF EXISTS");
    }

    @Test
    void findTop10PopularThemesWithinLastWeek_shouldReturnCorrectly() {
        List<Theme> top10PopularThemesWithinLastWeek = themeRepository.findTop10PopularThemesWithinLastWeek(
                TestFixture.makeFutureDate());
        for (Theme theme : top10PopularThemesWithinLastWeek) {
            System.out.println(theme.getId());
        }
    }
}
