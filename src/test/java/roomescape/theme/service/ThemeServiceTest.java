package roomescape.theme.service;

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

    @Test
    void 새로운_테마를_생성하고_쩡상적으로_응답을_반환한다() {
        ThemeRequest request = new ThemeRequest("공포 테마", "무서워", "무서워", LocalTime.of(2, 0));

        ThemeResponse response = themeService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("공포 테마");
        assertThat(response.getDescription()).isEqualTo("무서워");
        assertThat(response.getImageUrl()).isEqualTo("무서워");
    }

    @Test
    void 테마를_정상적으로_삭제한다() {
        ThemeRequest request = new ThemeRequest("코믹 테마", "웃겨", "웃겨", LocalTime.of(2, 0));
        ThemeResponse response = themeService.create(request);

        assertDoesNotThrow(() -> themeService.delete(response.getId()));
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

        ThemeResponse theme1 = themeService.create(new ThemeRequest("테마1", "설명1", "경로1", LocalTime.of(2, 0)));
        ThemeResponse theme2 = themeService.create(new ThemeRequest("테마2", "설명2", "경로2", LocalTime.of(2, 0)));

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                1L, theme1.getId(), yesterday, yesterday.plusHours(2));
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                2L, theme1.getId(), yesterday.plusHours(3), yesterday.plusHours(5));
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                3L, theme2.getId(), yesterday, yesterday.plusHours(2));

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
