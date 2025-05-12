package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.request.ThemeRequest;
import roomescape.model.Theme;
import roomescape.repository.ReservedThemeChecker;
import roomescape.repository.ThemeRepository;

class ThemeServiceTest {

    private ThemeRepository themeRepository;
    private ReservedThemeChecker reservedThemeChecker;
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        themeRepository = mock(ThemeRepository.class);
        reservedThemeChecker = mock(ReservedThemeChecker.class);
        themeService = new ThemeService(themeRepository, reservedThemeChecker);
    }

    @Test
    @DisplayName("테마를 추가할 수 있다.")
    void addThemeTest() {
        ThemeRequest dto = new ThemeRequest("스릴러", "진짜무서움", "aaa");
        Theme savedTheme = new Theme(1L, "스릴러", "진짜무서움", "aaa");

        when(themeRepository.addTheme(any())).thenReturn(savedTheme);

        Theme result = themeService.addTheme(dto);

        assertThat(result).isEqualTo(savedTheme);
    }

    @Test
    @DisplayName("모든 테마를 조회할 수 있다.")
    void getAllThemesTest() {
        Theme t1 = new Theme(1L, "공포", "무서움", "aaa");
        Theme t2 = new Theme(2L, "추리", "수사", "aaa");

        when(themeRepository.getAllThemes()).thenReturn(List.of(t1, t2));

        List<Theme> themes = themeService.getAllThemes();

        assertThat(themes).containsExactly(t1, t2);
    }

    @Test
    @DisplayName("예약된 테마를 삭제하려 하면 예외가 발생한다.")
    void deleteReservedThemeTest() {
        when(reservedThemeChecker.isReservedTheme(1L)).thenReturn(true);

        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Theme is already reserved.");
    }

    @Test
    @DisplayName("존재하지 않는 테마 삭제 시 예외가 발생한다.")
    void deleteNonExistentThemeTest() {
        when(reservedThemeChecker.isReservedTheme(1L)).thenReturn(false);
        when(themeRepository.deleteTheme(1L)).thenReturn(0);

        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("삭제할 테마가 존재하지 않습니다");
    }

    @Test
    @DisplayName("ID로 테마를 조회할 수 있다.")
    void getThemeByIdTest() {
        Theme theme = new Theme(1L, "공포", "겁나무서워요", "bbb");
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));

        Theme result = themeService.getThemeById(1L);

        assertThat(result).isEqualTo(theme);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 테마 조회 시 예외가 발생한다.")
    void getThemeByInvalidIdTest() {
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> themeService.getThemeById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 테마입니다");
    }

    @Test
    @DisplayName("최근 일주일 베스트 테마를 조회할 수 있다.")
    void getWeeklyBestThemesTest() {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        List<Long> bestIds = List.of(1L, 3L);
        List<Theme> bestThemes = List.of(
                new Theme(1L, "공포", "무서움", "aaa"),
                new Theme(3L, "진짜공포", "되게무서움", "aaa")
        );

        when(reservedThemeChecker.getBestThemesIdsInDays(now.minusDays(7), now)).thenReturn(bestIds);
        when(themeRepository.findAllByIdIn(bestIds)).thenReturn(bestThemes);

        List<Theme> result = themeService.getWeeklyBestThemes();

        assertThat(result).containsExactlyElementsOf(bestThemes);
    }
}
