package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.domain.Theme;
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
    void 존재하지_않는_테마_조회시_예외가_발생한다() {
        given(themeRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> themeService.find(999L))
                .isInstanceOf(IllegalArgumentException.class)
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
        assertThat(result.get(0).getName()).isEqualTo("공포");
    }

    @Test
    void 테마를_삭제한다() {
        themeService.delete(1L);

        verify(themeRepository).deleteById(1L);
    }
}
