package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static roomescape.exception.ThemeErrorCode.THEME_DELETE_CONFLICT;
import static roomescape.testFixture.Fixture.THEME_1;
import static roomescape.testFixture.Fixture.THEME_2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.application.dto.ThemeDto;
import roomescape.domain.Theme;
import roomescape.domain.repository.ThemeRepository;
import roomescape.exception.BusinessException;
import roomescape.exception.NotFoundException;
import roomescape.presentation.dto.request.ThemeRequest;

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
        List<ThemeDto> result = themeService.getAllThemes();

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("테마를 등록할 수 있다.")
    @Test
    void registerTheme() {
        // given
        ThemeRequest request = new ThemeRequest("공포", "호러호러", "thumbnail.jpg");
        given(themeRepository.save(any())).willReturn(1L);

        // when
        ThemeDto result = themeService.registerTheme(request);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("공포");
    }

    @DisplayName("존재하는 테마를 ID로 조회할 수 있다.")
    @Test
    void getThemeById_success() {
        // given
        Theme theme = Theme.of(1L, "공포", "무서운 방", "horror.jpg");
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));

        // when
        Theme result = themeService.getThemeById(1L);

        // then
        assertThat(result).isEqualTo(theme);
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
    }

    @DisplayName("최근 일주일간 인기 테마 랭킹을 조회할 수 있다.")
    @Test
    void getThemeRanking() {
        // given
        List<Theme> ranking = List.of(
                Theme.of(1L, "1등", "인기1", "a.jpg"),
                Theme.of(2L, "2등", "인기2", "b.jpg")
        );
        given(themeRepository.findThemeRanking(anyInt(), any(), any()))
                .willReturn(ranking);

        // when
        List<ThemeDto> result = themeService.getThemeRanking();

        // then
        assertThat(result).hasSize(2);
    }
}
