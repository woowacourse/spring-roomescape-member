package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"/test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @DisplayName("테마 저장")
    @Test
    void save() {
        final ThemeSaveRequest themeSaveRequest = new ThemeSaveRequest("감자", "설명", "섬네일");
        final ThemeResponse themeResponse = themeService.saveTheme(themeSaveRequest);
        assertThat(themeResponse).isEqualTo(new ThemeResponse(14L, "감자", "설명", "섬네일"));
    }

    @DisplayName("테마 조회")
    @Test
    void getThemes() {
        final List<ThemeResponse> themeResponses = themeService.getThemes();
        assertThat(themeResponses).hasSize(13);
    }

    @DisplayName("테마 삭제")
    @Test
    void deleteTheme() {
        themeService.deleteTheme(13L);
        assertThat(themeService.getThemes()).hasSize(12);
    }

    @DisplayName("존재하지 않는 테마 삭제")
    @Test
    void deleteNonExistTheme() {
        assertThatThrownBy(() -> themeService.deleteTheme(14L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }

    @DisplayName("예약이 존재하는 테마 삭제")
    @Test
    void deleteReservationExistTheme() {
        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약이 존재하는 테마는 삭제할 수 없습니다.");
    }

    @DisplayName("인기 테마 조회")
    @Test
    void getPopularThemes() {
        final List<ThemeResponse> themeResponses = themeService.getPopularThemes(LocalDate.parse("2024-05-01"));
        assertThat(themeResponses)
                .hasSize(10)
                .containsExactly(
                        new ThemeResponse(1L, "이름1", "설명1", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                        new ThemeResponse(2L, "이름2", "설명2", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                        new ThemeResponse(3L, "이름3", "설명3", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                        new ThemeResponse(4L, "이름4", "설명4", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                        new ThemeResponse(5L, "이름5", "설명5", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                        new ThemeResponse(6L, "이름6", "설명6", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                        new ThemeResponse(7L, "이름7", "설명7", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                        new ThemeResponse(8L, "이름8", "설명8", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                        new ThemeResponse(9L, "이름9", "설명9", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                        new ThemeResponse(10L, "이름10", "설명10", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
                );
    }
}
