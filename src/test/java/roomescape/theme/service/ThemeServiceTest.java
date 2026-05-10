package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.doamin.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.repository.ThemeTimeQueryRepository;
import roomescape.theme.service.dto.ThemeTimeAvailability;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private ThemeTimeQueryRepository themeTimeQueryRepository;

    @InjectMocks
    private ThemeService themeService;

    @DisplayName("인기 테마를 조회한다.")
    @Test
    void getPopularThemes() {
        List<Theme> themes = List.of(
                new Theme(1L, "공포", "무서운 테마", "https://image.test/1.png"),
                new Theme(2L, "추리", "머리 쓰는 테마", "https://image.test/2.png")
        );
        when(themeRepository.findPopularThemes(eq(2),
                any(LocalDate.class),
                any(LocalDate.class)))
                .thenReturn(themes);

        List<Theme> result = themeService.getPopularThemes(2);

        assertThat(result).isEqualTo(themes);
        verify(themeRepository).findPopularThemes(
                eq(2),
                any(LocalDate.class),
                any(LocalDate.class)
        );
    }

    @DisplayName("전체 테마를 조회할 수 있다.")
    @Test
    void getAllThemes() {
        List<Theme> themes = List.of(
                new Theme(1L, "공포", "무서운 테마", "https://image.test/1.png")
        );
        when(themeRepository.findAll()).thenReturn(themes);

        List<Theme> result = themeService.getAllThemes();

        assertThat(result).isEqualTo(themes);
    }

    @DisplayName("테마의 날짜별 예약 가능 시간을 조회할 수 있다.")
    @Test
    void getThemeTimeAvailability() {
        LocalDate date = LocalDate.of(2026, 5, 10);
        List<ThemeTimeAvailability> availabilities = List.of(
                new ThemeTimeAvailability(1L, LocalTime.of(10, 0), true),
                new ThemeTimeAvailability(2L, LocalTime.of(11, 0), false)
        );
        when(themeTimeQueryRepository.findThemeAvailableTime(1L, date)).thenReturn(availabilities);

        List<ThemeTimeAvailability> result = themeService.getThemeTimeAvailability(1L, date);

        assertThat(result).isEqualTo(availabilities);
    }
}
