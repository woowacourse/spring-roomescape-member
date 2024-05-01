package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Theme;
import roomescape.dto.ThemeResponse;
import roomescape.repository.ThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static roomescape.TestFixture.WOOTECO_THEME;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {
    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("테마를 생성한다.")
    void create() {
        // given
        Theme expectedTheme = WOOTECO_THEME(1L);

        BDDMockito.given(themeRepository.save(any()))
                .willReturn(expectedTheme);

        // when
        ThemeResponse response = themeService.create(expectedTheme);

        // then
        assertThat(response.id()).isEqualTo(expectedTheme.getId());
    }
}
