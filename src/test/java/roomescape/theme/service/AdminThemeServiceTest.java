package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeAddRequest;
import roomescape.exception.ClientIllegalArgumentException;
import roomescape.theme.repository.ThemeRepository;

class AdminThemeServiceTest {

    ThemeRepository themeRepository;
    AdminThemeService adminThemeService;

    @BeforeEach
    void setUp() {
        themeRepository = new FakeThemeRepository();
        adminThemeService = new AdminThemeService(themeRepository);
    }

    @DisplayName("모든 테마를 불러올 수 있습니다.")
    @Test
    void should_get_all_theme() {
        themeRepository.insert(new Theme(1L, "테마1", "테마1설명", "url"));

        List<Theme> allTheme = adminThemeService.findAllTheme();

        assertThat(allTheme.size()).isOne();
    }

    @DisplayName("테마를 추가할 수 있습니다.")
    @Test
    void should_add_theme() {
        Theme expectedTheme = new Theme(1L, "테마1", "테마1설명", "url");

        Theme savedTheme = adminThemeService.addTheme(new ThemeAddRequest("테마1", "테마1설명", "url"));

        assertThat(savedTheme).isEqualTo(expectedTheme);
    }

    @DisplayName("존재하지 않는 테마 삭제 요청시 예외가 발생합니다")
    @Test
    void should_throw_ClientIllegalArgumentException_when_theme_id_no_exist() {
        assertThatThrownBy(() -> adminThemeService.removeTheme(1L))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("해당 id를 가진 테마가 존재하지 않습니다.");
    }
}
