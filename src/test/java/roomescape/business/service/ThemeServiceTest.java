package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fake.FakeThemeDao;
import roomescape.presentation.dto.ThemeRequest;
import roomescape.presentation.dto.ThemeResponse;

class ThemeServiceTest {

    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        themeService = new ThemeService(new FakeThemeDao());
    }

    @DisplayName("테마를 저장한다.")
    @Test
    void create() {
        // given
        final ThemeRequest themeRequest = new ThemeRequest("테마", "소개", "썸네일");
        final ThemeResponse expected = new ThemeResponse(1L, "테마", "소개", "썸네일");

        // when & then
        assertThat(themeService.create(themeRequest))
                .isEqualTo(expected);
    }
}
