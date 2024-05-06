package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {
    @Mock
    private ThemeDao themeDao;
    @InjectMocks
    private ThemeService themeService;

    @DisplayName("모든 테마를 조회할 수 있다.")
    @Test
    void findThemesTest() {
        given(themeDao.findThemes()).willReturn(List.of(
                new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"),
                new Theme(2L, "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg")));

        assertThat(themeService.findThemes()).isEqualTo(List.of(
                new ThemeResponse(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"),
                new ThemeResponse(2L, "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg")));
    }

    @DisplayName("인기 테마를 조회할 수 있다.")
    @Test
    void findPopularThemesTest() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(7);
        LocalDate endDate = currentDate.minusDays(1);
        int count = 10;
        given(themeDao.findThemesSortedByCountOfReservation(startDate, endDate, count)).willReturn(List.of(
                new Theme(2L, "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg"),
                new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg")));

        assertThat(themeService.findPopularThemes()).isEqualTo(List.of(
                new ThemeResponse(2L, "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg"),
                new ThemeResponse(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg")));
    }

    @DisplayName("테마를 생성할 수 있다.")
    @Test
    void createThemeTest() {
        ThemeCreateRequest request = new ThemeCreateRequest("레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        given(themeDao.createTheme(request.createTheme()))
                .willReturn(new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"));
        ThemeResponse expected = new ThemeResponse(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");

        assertThat(themeService.createTheme(request)).isEqualTo(expected);
    }
}
