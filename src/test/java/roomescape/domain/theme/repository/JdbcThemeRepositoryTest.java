package roomescape.domain.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import roomescape.domain.theme.entity.Theme;

class JdbcThemeRepositoryTest {

    private JdbcThemeRepository themeRepository;
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
        jdbcTemplate = new JdbcTemplate(dataSource);
        timeInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("reservation_time")
            .usingColumns("start_at")
            .usingGeneratedKeyColumns("id");
    }

    @Nested
    class SaveTest {

        @Test
        void 성공() {
            // given
            Theme theme = Theme.create("테마1", "설명1", "image1.png");

            // when
            Theme actual = themeRepository.save(theme);

            // then
            assertThat(actual.getId()).isEqualTo(1L);
            assertThat(actual.getName()).isEqualTo("테마1");
            assertThat(actual.getDescription()).isEqualTo("설명1");
            assertThat(actual.getImageUrl()).isEqualTo("image1.png");
        }
    }

    @Nested
    class FindAllThemesTest {

        @Test
        void 성공() {
            // given
            Theme theme1 = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));
            Theme theme2 = themeRepository.save(Theme.create("테마2", "설명2", "image2.png"));

            // when
            List<Theme> actual = themeRepository.findAllThemes();

            // then
            assertThat(actual)
                .extracting(Theme::getId, Theme::getName, Theme::getDescription, Theme::getImageUrl)
                .containsExactly(
                    tuple(theme1.getId(), "테마1", "설명1", "image1.png"),
                    tuple(theme2.getId(), "테마2", "설명2", "image2.png")
                );
        }
    }

    @Nested
    class FindThemeByIdTest {

        @Test
        void 성공() {
            // given
            Theme savedTheme = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));

            // when
            Optional<Theme> actual = themeRepository.findThemeById(savedTheme.getId());

            // then
            assertThat(actual).isPresent();
            assertThat(actual.get().getId()).isEqualTo(savedTheme.getId());
            assertThat(actual.get().getName()).isEqualTo("테마1");
            assertThat(actual.get().getDescription()).isEqualTo("설명1");
            assertThat(actual.get().getImageUrl()).isEqualTo("image1.png");
        }

        @Test
        void 존재하지_않으면_빈_값을_반환한다() {
            // when
            Optional<Theme> actual = themeRepository.findThemeById(1L);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class DeleteThemeByIdTest {

        @Test
        void 성공() {
            // given
            Theme theme1 = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));
            Theme theme2 = themeRepository.save(Theme.create("테마2", "설명2", "image2.png"));

            // when
            themeRepository.deleteThemeById(theme1.getId());

            // then
            assertThat(themeRepository.findAllThemes())
                .extracting(Theme::getId)
                .containsExactly(theme2.getId());
        }
    }

    @Nested
    class FindPopularThemesDateBetweenTest {

        @Test
        void 예약_개수가_많은_순서대로_조회한다() {
            // given
            LocalDate startDate = LocalDate.of(2026, 5, 1);
            LocalDate endDate = LocalDate.of(2026, 5, 7);
            Theme theme1 = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));
            Theme theme2 = themeRepository.save(Theme.create("테마2", "설명2", "image2.png"));
            Theme theme3 = themeRepository.save(Theme.create("테마3", "설명3", "image3.png"));

            saveReservations(theme1, startDate, 3);
            saveReservations(theme2, startDate, 5);
            saveReservations(theme3, startDate, 1);

            // when
            List<Theme> actual = themeRepository.findPopularThemesDateBetween(startDate, endDate, 10);

            // then
            assertThat(actual)
                .extracting(Theme::getName)
                .containsExactly("테마2", "테마1", "테마3");
        }

        @Test
        void 조회_기간_밖의_예약은_인기순에_반영하지_않는다() {
            // given
            LocalDate startDate = LocalDate.of(2026, 5, 1);
            LocalDate endDate = LocalDate.of(2026, 5, 7);
            Theme theme1 = themeRepository.save(Theme.create("테마1", "설명1", "image1.png"));
            Theme theme2 = themeRepository.save(Theme.create("테마2", "설명2", "image2.png"));

            saveReservations(theme1, startDate.minusDays(1), 8);
            saveReservations(theme1, startDate, 1);
            saveReservations(theme2, startDate, 2);

            // when
            List<Theme> actual = themeRepository.findPopularThemesDateBetween(startDate, endDate, 10);

            // then
            assertThat(actual)
                .extracting(Theme::getName)
                .containsExactly("테마2", "테마1");
        }

        @Test
        void limit_개수만큼_조회한다() {
            // given
            LocalDate startDate = LocalDate.of(2026, 5, 1);
            LocalDate endDate = LocalDate.of(2026, 5, 7);
            for (int i = 1; i <= 12; i++) {
                Theme theme = themeRepository.save(Theme.create("테마" + i, "설명" + i, "image" + i + ".png"));
                saveReservations(theme, startDate, i);
            }

            // when
            List<Theme> actual = themeRepository.findPopularThemesDateBetween(startDate, endDate, 10);

            // then
            assertThat(actual).hasSize(10);
            assertThat(actual)
                .extracting(Theme::getName)
                .containsExactly("테마12", "테마11", "테마10", "테마9", "테마8", "테마7", "테마6", "테마5", "테마4",
                    "테마3");
        }
    }

    private void saveReservations(Theme theme, LocalDate date, int count) {
        for (int i = 0; i < count; i++) {
            Long timeId = saveTime(LocalTime.of(10, 0).plusMinutes(i));
            saveReservation("예약자" + theme.getId() + "-" + i, date, timeId, theme.getId());
        }
    }

    private Long saveTime(LocalTime startAt) {
        return timeInsert.executeAndReturnKey(Map.of("start_at", startAt)).longValue();
    }

    private void saveReservation(String name, LocalDate date, Long timeId, Long themeId) {
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
            name,
            date,
            timeId,
            themeId
        );
    }
}
