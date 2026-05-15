package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.controller.dto.ThemeFamousFindRequest;
import roomescape.domain.Theme;
import roomescape.exception.NotFoundException;
import roomescape.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @InjectMocks
    private ThemeService themeService;

    @Mock
    private ThemeRepository themeRepository;

    @Test
    void 테마를_생성한다() {
        ThemeCreateRequest request = new ThemeCreateRequest("공포", "무서운 테마", "https://example.com/img.jpg");
        Theme saved = Theme.of(1L, "공포", "무서운 테마", "https://example.com/img.jpg");
        given(themeRepository.save(any())).willReturn(saved);

        Theme result = themeService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("공포");
    }

    @Test
    void 존재하지_않는_테마_조회시_NotFoundException이_발생한다() {
        given(themeRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> themeService.find(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 테마입니다");
    }

    @Test
    void 전체_테마를_조회한다() {
        List<Theme> themes = List.of(
                Theme.of(1L, "공포", "desc", "url"),
                Theme.of(2L, "추리", "desc", "url")
        );
        given(themeRepository.findAll()).willReturn(themes);

        List<Theme> result = themeService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void 테마를_삭제한다() {
        themeService.delete(1L);

        verify(themeRepository).deleteById(1L);
    }

    @Test
    void 인기_테마를_기본값으로_조회한다() {
        List<Theme> themes = List.of(Theme.of(1L, "공포", "desc", "url"));
        given(themeRepository.findFamous(anyLong(), any(), anyLong())).willReturn(themes);

        ThemeFamousFindRequest request = new ThemeFamousFindRequest(null, null, null);
        List<Theme> result = themeService.findFamous(request);

        assertThat(result).hasSize(1);
        verify(themeRepository).findFamous(7L, LocalDate.now(), 10L);
    }

    @Test
    void 인기_테마를_파라미터로_조회한다() {
        List<Theme> themes = List.of(Theme.of(1L, "공포", "desc", "url"));
        given(themeRepository.findFamous(anyLong(), any(), anyLong())).willReturn(themes);

        ThemeFamousFindRequest request = new ThemeFamousFindRequest(3L, LocalDate.of(2025, 6, 1), 5L);
        List<Theme> result = themeService.findFamous(request);

        assertThat(result).hasSize(1);
        verify(themeRepository).findFamous(3L, LocalDate.of(2025, 6, 1), 5L);
    }
}
