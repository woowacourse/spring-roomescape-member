package roomescape.reservation.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JdbcThemeRepositoryTest {
    @Autowired
    private JdbcThemeRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    void 테마_저장_레포지토리_테스트() {
        Theme theme = new Theme(null, "무서운게 딱 좋아", "무서운 분위기의 방탈출", "https://example.com/theme.jpg");

        Theme savedTheme = repository.save(theme);
        Long id = jdbcTemplate.queryForObject("SELECT id FROM theme LIMIT 1", Long.class);

        assertThat(savedTheme.getId()).isEqualTo(id);
        assertThat(savedTheme.getName()).isEqualTo("무서운게 딱 좋아");
        assertThat(savedTheme.getDescription()).isEqualTo("무서운 분위기의 방탈출");
        assertThat(savedTheme.getThumbnailUrl()).isEqualTo("https://example.com/theme.jpg");
    }

    @Test
    @Transactional
    void 테마_삭제_레포지토리_테스트() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url) VALUES (?,?,?)", "무서운게 딱 좋아",
                "무서운 분위기의 방탈출", "https://example.com/theme.jpg");
        Long themeId = jdbcTemplate.queryForObject("SELECT id FROM theme LIMIT 1", Long.class);

        repository.deleteById(themeId);
        int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);

        assertThat(rowCount).isEqualTo(0);
    }

    @Test
    @Transactional
    void 각_날짜에_존재하는_모든_테마_조회_API_테스트(){
        // reservation_time 추가
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "16:00");

        // theme 추가
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url) VALUES (?,?,?)", "꿀잼 방탈출",
                "재밌는 분위기의 방탈출", "https://example.com/theme_happy.jpg");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url) VALUES (?,?,?)", "무서운게 딱 좋아",
                "무서운 분위기의 방탈출", "https://example.com/theme.jpg");
        Long firstThemeId = jdbcTemplate.queryForObject(
                "SELECT id FROM theme WHERE name = ?",
                Long.class,
                "꿀잼 방탈출"
        );
        Long secondThemeId = jdbcTemplate.queryForObject(
                "SELECT id FROM theme WHERE name = ?",
                Long.class,
                "무서운게 딱 좋아"
        );

        // schedule 추가
        Long firstTimeId = jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class,
                "15:00"
        );
        Long secondTimeId = jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class,
                "16:00"
        );
        jdbcTemplate.update("INSERT INTO schedule (date, time_id, theme_id) VALUES (?, ?, ?)", "2026-05-04", firstTimeId, firstThemeId);
        jdbcTemplate.update("INSERT INTO schedule (date, time_id, theme_id) VALUES (?, ?, ?)", "2026-05-04", secondTimeId, firstThemeId);
        jdbcTemplate.update("INSERT INTO schedule (date, time_id, theme_id) VALUES (?, ?, ?)", "2026-05-04", firstTimeId, secondThemeId);
        jdbcTemplate.update("INSERT INTO schedule (date, time_id, theme_id) VALUES (?, ?, ?)", "2026-05-05", secondTimeId, secondThemeId);

        List<Theme> themes = repository.findByDate(LocalDate.of(2026, 5, 4));

        assertThat(themes).hasSize(2);
        assertThat(themes)
                .extracting(Theme::getName)
                .containsExactly("꿀잼 방탈출", "무서운게 딱 좋아");
        assertThat(themes)
                .extracting(Theme::getId)
                .containsExactly(firstThemeId, secondThemeId);

    }
}
