package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static roomescape.TestFixture.HORROR_THEME;
import static roomescape.TestFixture.WOOTECO_THEME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.ThemeDao;
import roomescape.domain.theme.ThemeAggregationLimit;
import roomescape.domain.theme.ThemeAggregationPeriod;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.ThemeResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {
    @Mock
    private ThemeDao themeDao;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("테마를 생성한다.")
    void create() {
        // given
        final Theme expectedTheme = WOOTECO_THEME(1L);

        given(themeDao.save(any()))
                .willReturn(expectedTheme);

        // when
        final ThemeResponse response = themeService.create(expectedTheme);

        // then
        assertThat(response.id()).isEqualTo(expectedTheme.getId());
    }

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void findAll() {
        // given
        final List<Theme> expectedThemes = List.of(WOOTECO_THEME(1L));

        given(themeDao.findAll())
                .willReturn(expectedThemes);

        // when
        final List<ThemeResponse> responses = themeService.findAll();

        // then
        final ThemeResponse expectedResponse = ThemeResponse.from(WOOTECO_THEME(1L));
        assertThat(responses).hasSize(1)
                .containsExactly(expectedResponse);
    }

    @Test
    @DisplayName("Id로 테마를 조회한다.")
    void findById() {
        // given
        final Theme expectedTheme = WOOTECO_THEME(1L);

        given(themeDao.findById(anyLong()))
                .willReturn(Optional.of(expectedTheme));

        // when
        final ThemeResponse themeResponse = themeService.findById(1L);

        // then
        assertAll(() -> {
            assertThat(themeResponse.id()).isEqualTo(expectedTheme.getId());
            assertThat(themeResponse.name()).isEqualTo(expectedTheme.getName());
        });
    }

    @Test
    @DisplayName("조회하려는 테마가 존재하지 않는 경우 예외가 발생한다.")
    void throwExceptionWhenNotExistTheme() {
        // given
        given(themeDao.findById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> themeService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteById() {
        // given
        final Theme theme = WOOTECO_THEME(1L);

        given(themeDao.findById(anyLong()))
                .willReturn(Optional.of(theme));

        // when & then
        assertThatCode(() -> themeService.deleteById(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제하려는 테마가 존재하지 않는 경우 예외가 발생한다.")
    void throwExceptionWhenDeleteNotExistingTheme() {
        // given
        given(themeDao.findById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("최근 일주일을 기준으로 예약이 많은 순으로 테마 10개 목록을 조회한다.")
    void findAllPopular() {
        // given
        final List<Theme> expectedThemes = List.of(WOOTECO_THEME(1L), HORROR_THEME(2L));
        final LocalDate period = ThemeAggregationPeriod.calculateAggregationPeriod(LocalDate.now());
        final int limit = ThemeAggregationLimit.getAggregationLimit();

        given(themeDao.findTopThemesByReservationCountDuringPeriod(period, limit))
                .willReturn(expectedThemes);

        // when
        final List<ThemeResponse> responses = themeService.findPopularThemes();

        // then
        final ThemeResponse expectedWootecoTheme = ThemeResponse.from(WOOTECO_THEME(1L));
        final ThemeResponse expectedHorrorTheme = ThemeResponse.from(HORROR_THEME(2L));
        assertThat(responses).hasSize(2)
                .containsExactly(expectedWootecoTheme, expectedHorrorTheme);
    }
}
