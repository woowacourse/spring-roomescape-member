package roomescape.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.dto.ThemesResponse;
import roomescape.theme.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private ThemeService themeService;

    @Test
    void 인기_테마_조회() {
        LocalDate testDate = LocalDate.of(2026, 5, 10);
        Instant fixedInstant = testDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

        given(clock.instant()).willReturn(fixedInstant);
        given(clock.getZone()).willReturn(ZoneId.systemDefault());

        LocalDate expectedStart = testDate.minusDays(7);
        LocalDate expectedEnd = testDate.minusDays(1);
        int limit = 10;

        Theme theme = new Theme(5L, "초보자 방", "입문 테마", "s3.com");

        given(themeRepository.findPopularThemes(expectedStart, expectedEnd, limit))
                .willReturn(List.of(theme));

        ThemesResponse themes = themeService.readPopularThemes();

        assertThat(themes.themes()).hasSize(1);
        assertThat(themes.themes().get(0).id()).isEqualTo(5L);
        assertThat(themes.themes().get(0).name()).isEqualTo("초보자 방");
    }
}
