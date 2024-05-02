package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.request.ThemeRequest;
import roomescape.model.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ThemeServiceTest {

    ThemeService themeService = new ThemeService(new FakeThemeRepository());

    @DisplayName("테마를 조회한다.")
    @Test
    void should_find_all_themes() {
        assertThat(themeService.findAllThemes()).hasSize(3);
    }

    @DisplayName("테마를 저장한다.")
    @Test
    void should_add_theme() {
        ThemeRequest themeRequest = new ThemeRequest("에버", "공포", "공포.jpg");
        themeService.addTheme(themeRequest);
        assertThat(themeService.findAllThemes()).hasSize(4);
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void should_delete_theme() {
        themeService.deleteTheme(1L);
        assertThat(themeService.findAllThemes()).hasSize(2);
    }

    @DisplayName("최근 일주일 간 가장 인기 있는 테마 10개를 조회한다.")
    @Test
    void should_find_popular_theme_of_week() {
        List<Theme> popularThemes = themeService.findPopularThemes();
        assertThat(popularThemes).containsExactly(
                new Theme(10, "name10", "description10", "thumbnail10"),
                new Theme(9, "name9", "description9", "thumbnail9"),
                new Theme(1, "name1", "description1", "thumbnail1"),
                new Theme(2, "name2", "description2", "thumbnail2"),
                new Theme(3, "name3", "description3", "thumbnail3"),
                new Theme(4, "name4", "description4", "thumbnail4"),
                new Theme(5, "name5", "description5", "thumbnail5"),
                new Theme(6, "name6", "description6", "thumbnail6"),
                new Theme(7, "name7", "description7", "thumbnail7"),
                new Theme(8, "name8", "description8", "thumbnail8")
        );
    }
}