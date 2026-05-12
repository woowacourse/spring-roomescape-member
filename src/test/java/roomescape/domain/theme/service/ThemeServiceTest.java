package roomescape.domain.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.PopularThemeResult;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.theme.request.ThemeCreateRequest;
import roomescape.domain.theme.response.PopularThemesResponse;
import roomescape.domain.theme.response.ThemeResponse;

class ThemeServiceTest {

    private final ThemeRepository themeRepository = Mockito.mock(ThemeRepository.class);

    private final Clock fixedClock = Clock.fixed(
            LocalDate.of(2026, 5, 6)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant(),
            ZoneId.systemDefault()
    );

    private final ThemeService themeService = new ThemeService(themeRepository, fixedClock);

    @Test
    @DisplayName("테마를 성공적으로 생성한다.")
    void saveTheme() {
        // given
        Theme theme = Theme.of(1L, "테마", "요약", "www.url.com");
        ThemeCreateRequest request = new ThemeCreateRequest("테마", "요약", "www.url.com");

        when(themeRepository.save(any(Theme.class)))
                .thenReturn(theme);

        // when
        ThemeResponse response = themeService.saveTheme(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("테마");
        assertThat(response.description()).isEqualTo("요약");
        assertThat(response.thumbnailUrl()).isEqualTo("www.url.com");

        verify(themeRepository).save(any(Theme.class));
    }

    @Test
    @DisplayName("모든 테마를 조회한다.")
    void findAllThemes() {
        // given
        List<Theme> themes = List.of(
                Theme.of(1L, "테마A", "요약", "www.url.com/A"),
                Theme.of(2L, "테마B", "요약", "www.url.com/B")
        );

        when(themeRepository.findAll()).thenReturn(themes);

        // when
        List<ThemeResponse> responses = themeService.findAllThemes().themes();

        // then
        assertThat(responses).hasSize(2)
                .extracting("id", "name", "description", "thumbnailUrl")
                .containsExactly(
                        tuple(1L, "테마A", "요약", "www.url.com/A"),
                        tuple(2L, "테마B", "요약", "www.url.com/B")
                );

        verify(themeRepository).findAll();
    }

    @Test
    @DisplayName("최근 1주일간 인기 테마 상위 10개를 조회한다.")
    void findPopularThemes() {
        // given
        int period = 7;
        int limit = 10;

        LocalDate expectedStartDate = LocalDate.of(2026, 4, 29);
        LocalDate expectedEndDate = LocalDate.of(2026, 5, 5);

        List<PopularThemeResult> popularThemes = List.of(
                new PopularThemeResult(1L, "테마1", "설명1", "url1", 1),
                new PopularThemeResult(2L, "테마2", "설명2", "url2", 2),
                new PopularThemeResult(3L, "테마3", "설명3", "url3", 3),
                new PopularThemeResult(4L, "테마4", "설명4", "url4", 4),
                new PopularThemeResult(5L, "테마5", "설명5", "url5", 5),
                new PopularThemeResult(6L, "테마6", "설명6", "url6", 6),
                new PopularThemeResult(7L, "테마7", "설명7", "url7", 7),
                new PopularThemeResult(8L, "테마8", "설명8", "url8", 8),
                new PopularThemeResult(9L, "테마9", "설명9", "url9", 9),
                new PopularThemeResult(10L, "테마10", "설명10", "url10", 10)
        );

        when(themeRepository.findPopularThemes(expectedStartDate, expectedEndDate, limit))
                .thenReturn(popularThemes);

        // when
        PopularThemesResponse response = themeService.findPopularThemes(period, limit);

        // then
        assertThat(response.popularThemes()).hasSize(10)
                .extracting("id", "name", "rank")
                .containsExactly(
                        tuple(1L, "테마1", 1),
                        tuple(2L, "테마2", 2),
                        tuple(3L, "테마3", 3),
                        tuple(4L, "테마4", 4),
                        tuple(5L, "테마5", 5),
                        tuple(6L, "테마6", 6),
                        tuple(7L, "테마7", 7),
                        tuple(8L, "테마8", 8),
                        tuple(9L, "테마9", 9),
                        tuple(10L, "테마10", 10)
                );

        verify(themeRepository).findPopularThemes(expectedStartDate, expectedEndDate, limit);
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteThemeById() {
        // given
        Long themeId = 1L;

        // when
        themeService.deleteThemeById(themeId);

        // then
        verify(themeRepository).deleteById(themeId);
    }

    @Test
    @DisplayName("예약이 존재하는 테마를 삭제하면 예외가 발생한다.")
    void deleteThemeById_throwsException_whenReservationExists() {
        // given
        Long themeId = 1L;
        doThrow(DataIntegrityViolationException.class)
                .when(themeRepository).deleteById(themeId);

        // when & then
        assertThatThrownBy(() -> themeService.deleteThemeById(themeId))
                .isInstanceOf(IllegalStateException.class);
    }
}
