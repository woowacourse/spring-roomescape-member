package roomescape.theme.service;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.ThemeSaveServiceDto;

@ExtendWith(MockitoExtension.class)
class ThemeServiceImplTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeServiceImpl themeService;

    @Test
    void getThemes() {
        List<Theme> themes = List.of(
                new Theme("a", "b", "c").withId(1L),
                new Theme("d", "e", "f").withId(2L));
        when(themeRepository.findAll()).thenReturn(themes);

        assertThat(themeService.getThemes()).isEqualTo(themes);
        verify(themeRepository).findAll();
    }

    @Test
    void save() {
        ThemeSaveServiceDto dto = new ThemeSaveServiceDto("이름", "설명", "https://url");
        Theme persisted = new Theme("이름", "설명", "https://url").withId(10L);
        when(themeRepository.save(any(Theme.class))).thenReturn(persisted);

        Theme result = themeService.save(dto);

        assertThat(result).isEqualTo(persisted);

        ArgumentCaptor<Theme> captor = ArgumentCaptor.forClass(Theme.class);
        verify(themeRepository).save(captor.capture());
        Theme passed = captor.getValue();
        assertThat(passed.getId()).isNull();
        assertThat(passed.getName()).isEqualTo("이름");
        assertThat(passed.getDescription()).isEqualTo("설명");
        assertThat(passed.getImageUrl()).isEqualTo("https://url");
    }

    @Test
    void deleteById() {
        when(themeRepository.deleteById(1L)).thenReturn(true);

        themeService.deleteById(1L);

        verify(themeRepository).deleteById(1L);
    }

    @Test
    void deleteById_없으면_예외() {
        when(themeRepository.deleteById(99L)).thenReturn(false);

        assertThatThrownBy(() -> themeService.deleteById(99L))
                .isInstanceOf(ThemeNotFoundException.class)
                .hasMessage("테마가 존재하지 않습니다. id=99")
                .extracting(e -> ((ThemeNotFoundException) e).getId())
                .isEqualTo(99L);
    }
}
