package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.service.stub.FakeThemeRepository;
import roomescape.theme.exception.ThemeDuplicateException;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.ThemeService;

class ThemeServiceTest {

    private ThemeService themeService;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new FakeThemeRepository();
        themeService = new ThemeService(themeRepository);
    }

    @Test
    @DisplayName("같은 이름 테마 중복 생성 예외")
    void save_whenDuplicateName_throws() {
        themeService.save("미술관의 밤", "설명", "thumb");

        assertThatThrownBy(() -> themeService.save("미술관의 밤", "다른 설명", "thumb2"))
                .isInstanceOf(ThemeDuplicateException.class);
    }

    @Test
    @DisplayName("없는 테마 ID 조회 시 예외")
    void getById_whenNotFound_throws() {
        assertThatThrownBy(() -> themeService.getById(999L))
                .isInstanceOf(ThemeNotFoundException.class);
    }

}
