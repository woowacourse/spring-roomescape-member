package roomescape.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
}
