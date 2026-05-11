package roomescape.service;

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
    void 테마가_없으면_빈리스트_반환() {
        when(themeRepository.findAll()).thenReturn(Collections.emptyList());
        List<Theme> themes = themeService.getThemes();

        assertThat(themes.size()).isEqualTo(0);
    }

    @Test
    void 테마가_3개면_결과_반환() {
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
    void id로_테마를_조회한다() {
        Theme theme = new Theme(1L, "escape1", "방탈출1", "http://example.com/img1.jpg");

        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
        Theme result = themeService.findById(1L);

        assertThat(result).isEqualTo(theme);
    }

    @Test
    void 없는_id로_조회하면_예외() {
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> themeService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테마를_저장하면_저장된_테마_반환() {
        Theme theme = new Theme(null, "escape1", "방탈출1", "http://example.com/img1.jpg");
        Theme savedTheme = new Theme(1L, "escape1", "방탈출1", "http://example.com/img1.jpg");

        when(themeRepository.save(theme)).thenReturn(savedTheme);
        Theme result = themeService.saveTheme(theme);

        assertThat(result).isEqualTo(savedTheme);
    }

    @Test
    void id로_테마를_삭제한다() {
        themeService.deleteTheme(1L);

        verify(themeRepository).delete(1L);
    }

    @Test
    void 날짜가_있으면_예약_가능_시간을_조회한다() {
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
    void 인기_테마를_조회한다() {
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
