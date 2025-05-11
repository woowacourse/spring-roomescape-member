package roomescape.theme.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static roomescape.theme.exception.ThemeErrorCode.THEME_DELETE_CONFLICT;
import static roomescape.testFixture.Fixture.THEME_1;
import static roomescape.testFixture.Fixture.THEME_2;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.theme.application.dto.ThemeDto;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;
import roomescape.global.exception.BusinessException;
import roomescape.global.exception.NotFoundException;
import roomescape.theme.presentation.controller.ThemeRankingCondition;
import roomescape.theme.presentation.dto.request.ThemeRequest;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {
    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @DisplayName("모든 테마를 조회할 수 있다.")
    @Test
    void getAllThemes() {
        // given
        given(themeRepository.findAll()).willReturn(List.of(THEME_1, THEME_2));

        // when
        themeService.getAllThemes();

        // then
        verify(themeRepository).findAll();
    }

    @DisplayName("테마를 등록할 수 있다.")
    @Test
    void registerTheme() {
        // given
        ThemeRequest request = new ThemeRequest("공포", "호러호러", "thumbnail.jpg");
        given(themeRepository.save(any(Theme.class))).willReturn(1L);

        // when
        ThemeDto result = themeService.registerTheme(request);

        // then
        assertThat(result.name()).isEqualTo("공포");
        verify(themeRepository).save(any(Theme.class));
    }

    @DisplayName("존재하는 테마를 ID로 조회할 수 있다.")
    @Test
    void getThemeById_success() {
        // given
        long themeId = 1L;
        Theme theme = Theme.of(themeId, "공포", "무서운 방", "horror.jpg");
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));

        // when
        Theme result = themeService.getThemeById(themeId);

        // then
        assertThat(result).isEqualTo(theme);
        verify(themeRepository).findById(themeId);
    }

    @DisplayName("존재하지 않는 테마 ID로 조회 시 예외가 발생한다.")
    @Test
    void getThemeById_fail() {
        // given
        given(themeRepository.findById(1L)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> themeService.getThemeById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("예약이 없는 테마는 정상적으로 삭제할 수 있다.")
    @Test
    void deleteTheme_success() {
        // when
        themeService.deleteTheme(1L);

        // then
        verify(themeRepository).deleteById(1L);
    }

    @DisplayName("예약이 걸린 테마 삭제 시 비즈니스 예외가 발생한다.")
    @Test
    void deleteTheme_fail_dueToForeignKey() {
        // given
        doThrow(DataIntegrityViolationException.class)
                .when(themeRepository).deleteById(1L);

        // then
        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(THEME_DELETE_CONFLICT.getMessage());
        verify(themeRepository).deleteById(1L);
    }

    @DisplayName("인기 테마 조회 시 결과를 dto로 감싸서 반환한다.")
    @Test
    void getThemeRanking() {
        // given
        List<Theme> ranking = List.of(
                Theme.of(1L, "1등", "인기1", "a.jpg"),
                Theme.of(2L, "2등", "인기2", "a.jpg"),
                Theme.of(3L, "3등", "인기3", "a.jpg")
        );

        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 7);
        int limit = 5;

        ThemeRankingCondition condition = ThemeRankingCondition.ofRequestParams(start, end, limit);

        given(themeRepository.findThemeRanking(start, end, limit)).willReturn(ranking);

        // when
        List<ThemeDto> result = themeService.getThemeRanking(condition);

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).name()).isEqualTo("1등");

        verify(themeRepository).findThemeRanking(start, end, limit);
    }
}
