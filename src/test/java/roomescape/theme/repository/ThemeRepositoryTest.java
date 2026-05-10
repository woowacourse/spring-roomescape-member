package roomescape.theme.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.model.Theme;

import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ThemeRepositoryTest {

    private ThemeRepository themeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        themeRepository = new ThemeRepository(jdbcTemplate);

        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM schedule");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM \"USER\"");
    }

    @Test
    void 테마를_데이터베이스에_성공적으로_저장하고_생성된_ID를_반환한다() {
        // given
        Theme theme = new Theme("테마", "설명", "경로", LocalTime.of(2, 0));

        // when
        Long savedId = themeRepository.create(theme);

        // then
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
        // given
        // 🚨 ID 없이 객체를 생성합니다. ID는 DB가 부여할 것입니다.
        Theme theme1 = new Theme("테마1", "설명1", "경로1", LocalTime.of(2, 0));
        Theme theme2 = new Theme("테마2", "설명2", "경로2", LocalTime.of(2, 0));

        themeRepository.create(theme1);
        themeRepository.create(theme2);

        // when
        List<Theme> themes = themeRepository.findAll();

        // then
        assertThat(themes).isNotNull();
        assertThat(themes).hasSize(2);

        assertThat(themes).extracting(Theme::getName)
                .containsExactlyInAnyOrder("테마1", "테마2");
    }

    @Test
    void 최근_예약이_많은_순서대로_인기_테마를_조회한다() {
        jdbcTemplate.update("INSERT INTO \"USER\" (id, name, role) VALUES (?, ?, ?)", 1L, "user1", "USER");

        Long themeId1 = themeRepository.create(new Theme("테마1", "설명1", "경로1", LocalTime.of(2, 0)));
        Long themeId2 = themeRepository.create(new Theme("테마2", "설명2", "경로2", LocalTime.of(2, 0)));

        LocalDateTime yesterday = LocalDate.now().minusDays(1).atTime(10, 0);

        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                1L, themeId1, yesterday, yesterday.plusHours(2));
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                2L, themeId1, yesterday.plusHours(3), yesterday.plusHours(5));
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                3L, themeId2, yesterday, yesterday.plusHours(2));

        jdbcTemplate.update("INSERT INTO reservation (schedule_id, user_id) VALUES (?, ?)", 1L, 1L);
        jdbcTemplate.update("INSERT INTO reservation (schedule_id, user_id) VALUES (?, ?)", 2L, 1L);
        jdbcTemplate.update("INSERT INTO reservation (schedule_id, user_id) VALUES (?, ?)", 3L, 1L);

        List<PopularThemeResponse> popularThemes = themeRepository.findPopularThemes("reservations", 10, 7);

        assertThat(popularThemes).hasSize(2);
        assertThat(popularThemes.get(0).getThemeName()).isEqualTo("테마1");
        assertThat(popularThemes.get(0).getReservationCount()).isEqualTo(2);
        assertThat(popularThemes.get(1).getThemeName()).isEqualTo("테마2");
        assertThat(popularThemes.get(1).getReservationCount()).isEqualTo(1);
    }
}
