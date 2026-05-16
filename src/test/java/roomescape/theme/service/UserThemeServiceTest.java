package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.ServiceIntegrationTest;
import roomescape.theme.domain.SortType;
import roomescape.theme.domain.SortOrder;
import roomescape.theme.domain.Theme;

class UserThemeServiceTest extends ServiceIntegrationTest {

    @Autowired
    private UserThemeService userThemeService;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "INSERT INTO themes (name, description, thumbnail) VALUES ('Theme A', 'Desc', 'https://a.png')");
        jdbcTemplate.update(
                "INSERT INTO themes (name, description, thumbnail) VALUES ('Theme B', 'Desc', 'https://b.png')");
        jdbcTemplate.update(
                "INSERT INTO themes (name, description, thumbnail) VALUES ('Theme C', 'Desc', 'https://c.png')");
        jdbcTemplate.update(
                "INSERT INTO themes (name, description, thumbnail) VALUES ('Theme D', 'Desc', 'https://d.png')");
        jdbcTemplate.update(
                "INSERT INTO themes (name, description, thumbnail) VALUES ('Theme E', 'Desc', 'https://e.png')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00:00')");

        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User', '2026-05-06', 1, 1)");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User', '2026-05-07', 1, 1)");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User', '2026-05-08', 1, 1)");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User', '2026-05-09', 1, 1)");

        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User', '2026-05-06', 1, 2)");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User', '2026-05-07', 1, 2)");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User', '2026-05-08', 1, 2)");

        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User', '2026-05-06', 1, 3)");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User', '2026-05-07', 1, 3)");

        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User', '2026-05-06', 1, 4)");
    }

    @Test
    void 전체_테마를_조회할_수_있다() {
        List<Theme> themes = userThemeService.getAllThemes();

        assertThat(themes).hasSize(5);
    }

    @Test
    void 테마_랭킹을_조회할_수_있다() {
        List<Theme> ranked = userThemeService.getThemes(
                SortType.RESERVATION_COUNT, SortOrder.DESC,
                LocalDate.of(2026, 5, 6), LocalDate.of(2026, 5, 12), 10L);

        assertThat(ranked).hasSize(4);
        assertThat(ranked.get(0).id()).isEqualTo(1L);
        assertThat(ranked.get(1).id()).isEqualTo(2L);
        assertThat(ranked.get(2).id()).isEqualTo(3L);
        assertThat(ranked.get(3).id()).isEqualTo(4L);
    }

    @Test
    void limit_파라미터로_결과_수를_제한할_수_있다() {
        List<Theme> ranked = userThemeService.getThemes(
                SortType.RESERVATION_COUNT, SortOrder.DESC,
                LocalDate.of(2026, 5, 6), LocalDate.of(2026, 5, 12), 2L);

        assertThat(ranked).hasSize(2);
        assertThat(ranked.get(0).id()).isEqualTo(1L);
        assertThat(ranked.get(1).id()).isEqualTo(2L);
    }

    @Test
    void 날짜_범위_밖의_예약은_랭킹에서_제외된다() {
        List<Theme> ranked = userThemeService.getThemes(
                SortType.RESERVATION_COUNT, SortOrder.DESC,
                LocalDate.of(2026, 5, 8), LocalDate.of(2026, 5, 9), 10L);

        assertThat(ranked).hasSize(2);
        assertThat(ranked.get(0).id()).isEqualTo(1L);
        assertThat(ranked.get(1).id()).isEqualTo(2L);
    }
}
