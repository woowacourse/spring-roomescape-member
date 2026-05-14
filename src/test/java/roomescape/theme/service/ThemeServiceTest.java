package roomescape.theme.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.controller.dto.ThemeRankResponse;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.repository.dto.GetThemeRankingsInRecentDaysParams;
import roomescape.util.fixture.ThemeFixture;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @InjectMocks
    ThemeService themeService;

    @Mock
    ThemeRepository themeRepository;

    @Test
    void 인기_테마_순위를_조회한다() {
        //given
        int days = 10;
        int limit = 2;
        Theme themeA = ThemeFixture.createByIdAndName(1L, "themeA");
        Theme themeB = ThemeFixture.createByIdAndName(2L, "themeB");

        when(themeRepository.findThemesOrderedByReservationCount(any(GetThemeRankingsInRecentDaysParams.class)))
                .thenReturn(List.of(themeA, themeB));

        //when
        List<ThemeRankResponse> themeRankingsInRecentDays = themeService.getThemeRankingsInRecentDays(days, limit);

        ThemeRankResponse responseA = new ThemeRankResponse(1, ThemeResponse.from(themeA));
        ThemeRankResponse responseB = new ThemeRankResponse(2, ThemeResponse.from(themeB));

        //then
        Assertions.assertThat(themeRankingsInRecentDays)
                .containsExactly(responseA, responseB);
    }
}
