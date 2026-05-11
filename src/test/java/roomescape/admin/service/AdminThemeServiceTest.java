package roomescape.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.doamin.Theme;
import roomescape.theme.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class AdminThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private AdminThemeService adminThemeService;

    @DisplayName("관리자는 테마를 저장할 수 있다.")
    @Test
    void saveTheme() {
        when(themeRepository.save(any(Theme.class))).thenReturn(1L);

        long savedId = adminThemeService.saveTheme("공포", "무서운 테마", "https://image.test/theme.png");

        assertThat(savedId).isEqualTo(1L);
        verify(themeRepository).save(any(Theme.class));
    }

    @DisplayName("관리자는 테마를 삭제할 수 있다.")
    @Test
    void deleteTheme() {
        adminThemeService.deleteTheme(1L);

        verify(themeRepository).delete(1L);
    }
}
