package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {
    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("테마가 없으면 빈 목록을 반환한다")
    void findEmptyThemes() {
        when(themeRepository.findAll()).thenReturn(Collections.emptyList());
        List<Theme> themes = themeService.getThemes();

        assertThat(themes.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("저장된 테마 목록을 반환한다")
    void findThemes() {
        List<Theme> themes = List.of(
                new Theme(1L, "escape1", "방탈출1", "http://example.com/img1.jpg"),
                new Theme(2L, "escape2", "방탈출2", "http://example.com/img2.jpg"),
                new Theme(3L, "escape3", "방탈출3", "http://example.com/img3.jpg")
        );

        when(themeRepository.findAll()).thenReturn(themes);
        List<Theme> result = themeService.getThemes();

        assertThat(result.size()).isEqualTo(3);
        assertThat(result).isEqualTo(themes);
    }

    @Test
    @DisplayName("id로 테마를 조회한다")
    void findThemeById() {
        Theme theme = new Theme(1L, "escape1", "방탈출1", "http://example.com/img1.jpg");

        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
        Theme result = themeService.findById(1L);

        assertThat(result).isEqualTo(theme);
    }

    @Test
    @DisplayName("존재하지 않는 id로 테마를 조회하면 예외가 발생한다")
    void throwException_WhenThemeNotFoundById() {
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> themeService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테마를 저장하면 저장된 테마를 반환한다")
    void saveTheme() {
        Theme theme = new Theme(null, "escape1", "방탈출1", "http://example.com/img1.jpg");
        Theme savedTheme = new Theme(1L, "escape1", "방탈출1", "http://example.com/img1.jpg");

        when(themeRepository.save(theme)).thenReturn(savedTheme);
        Theme result = themeService.saveTheme(theme);

        assertThat(result).isEqualTo(savedTheme);
    }

    @Test
    @DisplayName("id로 테마를 삭제한다")
    void deleteThemeById() {
        themeService.deleteTheme(1L);

        verify(themeRepository).delete(1L);
    }

    @Test
    @DisplayName("테마와 날짜로 예약 가능 시간을 조회한다")
    void findAvailableTimesByThemeAndDate() {
        LocalDate date = LocalDate.of(2026, 5, 3);
        List<ReservationTime> times = List.of(
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new ReservationTime(2L, LocalTime.of(11, 0))
        );

        when(themeRepository.findAvailableTimes(1L, date)).thenReturn(times);
        List<ReservationTime> result = themeService.getAvailableTimes(1L, date);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(times);
    }

    @Test
    @DisplayName("인기 테마를 조회한다")
    void findPopularThemes() {
        LocalDate today = LocalDate.now();
        List<Theme> themes = List.of(
                new Theme(1L, "escape1", "방탈출1", "http://example.com/img1.jpg"),
                new Theme(2L, "escape2", "방탈출2", "http://example.com/img2.jpg")
        );

        when(themeRepository.findPopularThemes(today.minusDays(7), today.minusDays(1), 10)).thenReturn(themes);
        List<Theme> result = themeService.findPopularThemes();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(themes);
    }
}
