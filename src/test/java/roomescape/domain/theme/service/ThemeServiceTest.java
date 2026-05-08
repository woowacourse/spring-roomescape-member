package roomescape.domain.theme.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import roomescape.domain.theme.dto.request.ThemeCreateRequestDTO;
import roomescape.domain.theme.dto.response.ThemeResponseDTO;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.JdbcThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;

class ThemeServiceTest {

    private ThemeService themeService;
    private ThemeRepository themeRepository;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert timeInsert;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
            "jdbc:h2:mem:" + System.nanoTime() + ";MODE=MySQL;DB_CLOSE_DELAY=-1",
            "sa",
            ""
        );

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);

        themeRepository = new JdbcThemeRepository(dataSource);
        themeService = new ThemeService(themeRepository);
        jdbcTemplate = new JdbcTemplate(dataSource);
        timeInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("reservation_time")
            .usingColumns("start_at")
            .usingGeneratedKeyColumns("id");
    }

    @Nested
    class GetThemesTest {

        @Test
        void 성공() {
            // when
            List<ThemeResponseDTO> actual = themeService.getThemes();

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class GetPopularThemesTest {

        @Test
        void 성공() {
            // given
            LocalDate targetDate = LocalDate.now();
            for (int i = 1; i <= 15; i++) {
                Theme theme = themeRepository.save(Theme.create("테마" + i, "설명" + i, "https://image.com/" + i + ".png"));
                saveReservations(theme, targetDate, (16 - i) * 5);
            }

            // when
            List<ThemeResponseDTO> actual = themeService.getPopularThemes();

            // then
            assertThat(actual)
                .extracting(ThemeResponseDTO::name)
                .containsExactly("테마1", "테마2", "테마3", "테마4", "테마5", "테마6", "테마7", "테마8", "테마9", "테마10");
        }
    }

    @Nested
    class SaveThemeTest {

        @Test
        void 성공() {
            // given
            ThemeCreateRequestDTO request = new ThemeCreateRequestDTO(
                "피온",
                "테마 설명",
                "https://roomescape.com/images/themes/prison-room.png"
            );

            // when
            ThemeResponseDTO actual = themeService.saveTheme(request);

            // then
            assertThat(actual).isEqualTo(new ThemeResponseDTO(
                1L,
                "피온",
                "테마 설명",
                "https://roomescape.com/images/themes/prison-room.png"
            ));
        }
    }

    @Nested
    class DeleteThemeTest {

        @Test
        void 성공() {
            // given
            themeRepository.save(Theme.create("브라운", "테마 설명", "https://roomescape.com/images/themes/prison-room.png"));

            // when
            themeService.deleteThemeById(1L);
            List<ThemeResponseDTO> actual = themeService.getThemes();

            // then
            assertThat(actual).isEmpty();
        }
    }

    private void saveReservations(Theme theme, LocalDate date, int count) {
        for (int i = 0; i < count; i++) {
            Long timeId = timeInsert.executeAndReturnKey(Map.of("start_at", LocalTime.of(10, 0).plusMinutes(i)))
                .longValue();
            jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "예약자" + theme.getId() + "-" + i,
                date,
                timeId,
                theme.getId()
            );
        }
    }
}
