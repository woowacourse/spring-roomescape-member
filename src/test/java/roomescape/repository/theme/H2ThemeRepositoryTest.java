package roomescape.repository.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.test.fixture.DateFixture.TODAY;
import static roomescape.test.fixture.DateFixture.YESTERDAY;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Theme;

@JdbcTest
@Import(H2ThemeRepository.class)
class H2ThemeRepositoryTest {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private H2ThemeRepository themeRepository;

    @BeforeEach
    void setup() {
        template.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("모든 테마를 조회할 수 있다")
    @Test
    void canFindAll() {
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마2", "설명2", "썸네일2");
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마3", "설명3", "썸네일3");

        assertThat(themeRepository.findAll()).hasSize(3);
    }

    @DisplayName("ID를 기반으로 테마를 조회할 수 있다")
    @Test
    void canFindById() {
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");

        Optional<Theme> actualTheme = themeRepository.findById(1L);
        Theme expectedTheme = new Theme(1L, "테마1", "설명1", "썸네일1");
        assertAll(
                () -> assertThat(actualTheme).isPresent(),
                () -> assertThat(actualTheme.get()).isEqualTo(expectedTheme)
        );
    }

    @DisplayName("테마를 추가할 수 있다")
    @Test
    void canAddTheme() {
        Theme theme = Theme.createWithoutId("테마1", "설명1", "섬네일1");

        themeRepository.addTheme(theme);

        assertThat(themeRepository.findAll()).hasSize(1);
    }

    @DisplayName("테마를 삭제할 수 있다")
    @Test
    void canDeleteById() {
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");

        themeRepository.deleteById(1L);

        assertThat(themeRepository.findAll()).isEmpty();
    }

    @DisplayName("인기있는 테마를 조회할 수 있다")
    @Test
    void canGetTopThemesByCount() {
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        for (int i = 0; i < 15; i++) {
            template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                    "테마" + i, "설명", "썸네일");
        }

        template.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이름1", YESTERDAY.toString(), 1L, 1L);
        template.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이름1", YESTERDAY.toString(), 1L, 1L);
        template.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "이름1", YESTERDAY.toString(), 1L, 2L);

        List<Theme> topThemes = themeRepository.getTopThemesByCount(YESTERDAY.minusDays(1), TODAY);

        assertAll(
                () -> assertThat(topThemes).hasSize(10),
                () -> assertThat(topThemes.get(0).getName()).isEqualTo("테마0"),
                () -> assertThat(topThemes.get(1).getName()).isEqualTo("테마1")
        );
    }
}
