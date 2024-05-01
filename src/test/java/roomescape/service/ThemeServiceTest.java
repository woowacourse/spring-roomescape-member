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

import java.util.List;

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

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void findAll() {
        // given
        List<Theme> expectedThemes = List.of(WOOTECO_THEME(1L));

        BDDMockito.given(themeRepository.findAll())
                .willReturn(expectedThemes);

        // when
        List<ThemeResponse> responses = themeService.findAll();

        // then
        ThemeResponse expectedResponse = ThemeResponse.from(WOOTECO_THEME(1L));
        assertThat(responses).hasSize(1)
                .containsExactly(expectedResponse);
    }
}
