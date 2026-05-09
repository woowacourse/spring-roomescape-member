package roomescape.theme.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.dto.PopularThemesResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.dto.ThemesResponse;
import roomescape.theme.model.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM schedule");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM \"USER\"");
    }

    @Test
    void 새로운_테마를_생성하고_ID를_반환한다() {
        ThemeRequest request = new ThemeRequest("공포 테마", "무서워", "무서워", LocalTime.of(2, 0));

        Long createdId = themeService.create(request);

        assertThat(createdId).isNotNull();
        assertThat(createdId).isGreaterThan(0L);
    }

    @Test
    void 테마를_정상적으로_삭제한다() {
        ThemeRequest request = new ThemeRequest("코믹 테마", "웃겨", "웃겨", LocalTime.of(2, 0));
        Long themeId = themeService.create(request);

        assertDoesNotThrow(() -> themeService.delete(themeId));
    }

    @Test
    void 테마를_전체_조회한다() {
        Theme theme1 = new Theme(1L, "테마1", "설명1", "경로1", LocalTime.of(2, 0));
        Theme theme2 = new Theme(2L, "테마2", "설명2", "경로2", LocalTime.of(2, 0));

        ThemeRequest theme1Request = new ThemeRequest(theme1.getName(), theme1.getDescription(), theme1.getImageUrl(), theme1.getRequiredTime());
        ThemeRequest theme2Request = new ThemeRequest(theme2.getName(), theme2.getDescription(), theme2.getImageUrl(), theme2.getRequiredTime());

        themeService.create(theme1Request);
        themeService.create(theme2Request);

        ThemesResponse response = themeService.findAll();

        assertThat(response).isNotNull();
        assertThat(response.getThemeResponses()).hasSize(2);
    }

    @Test
    void 최근_일주일간_예약이_많은_순서대로_인기_테마를_조회한다() {
        jdbcTemplate.update("INSERT INTO \"USER\" (id, name, role) VALUES (?, ?, ?)", 1L, "user1", "USER");

        Long theme1Id = themeService.create(new ThemeRequest("테마1", "설명1", "경로1", LocalTime.of(2, 0)));
        Long theme2Id = themeService.create(new ThemeRequest("테마2", "설명2", "경로2", LocalTime.of(2, 0)));

        LocalDateTime yesterday = LocalDate.now().minusDays(1).atTime(10, 0);

        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                1L, theme1Id, yesterday, yesterday.plusHours(2));
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                2L, theme1Id, yesterday.plusHours(3), yesterday.plusHours(5));
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                3L, theme2Id, yesterday, yesterday.plusHours(2));

        jdbcTemplate.update("INSERT INTO reservation (schedule_id, user_id) VALUES (?, ?)", 1L, 1L);
        jdbcTemplate.update("INSERT INTO reservation (schedule_id, user_id) VALUES (?, ?)", 2L, 1L);
        jdbcTemplate.update("INSERT INTO reservation (schedule_id, user_id) VALUES (?, ?)", 3L, 1L);

        PopularThemesResponse response = themeService.findPopularThemes("reservations", 10, 7);

        assertThat(response).isNotNull();
        assertThat(response.getPopularThemeResponses()).hasSize(2);
        assertThat(response.getPopularThemeResponses().get(0).getThemeName()).isEqualTo("테마1");
        assertThat(response.getPopularThemeResponses().get(0).getReservationCount()).isEqualTo(2);
        assertThat(response.getPopularThemeResponses().get(1).getThemeName()).isEqualTo("테마2");
        assertThat(response.getPopularThemeResponses().get(1).getReservationCount()).isEqualTo(1);
    }
}
