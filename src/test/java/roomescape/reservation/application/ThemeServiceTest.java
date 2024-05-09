package roomescape.reservation.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.global.exception.NotFoundException;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.ThemeRepository;
import roomescape.reservation.dto.response.ThemeResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static roomescape.TestFixture.HORROR_THEME;
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

    @Test
    @DisplayName("Id로 조회하려는 테마가 존재하지 않는 경우 예외가 발생한다.")
    void findByNotExistId() {
        // given
        BDDMockito.given(themeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> themeService.findById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteById() {
        // given
        Long themeId = 1L;

        // when & then
        assertThatCode(() -> themeService.deleteById(themeId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("최근 일주일을 기준으로 예약이 많은 순으로 테마 10개 목록을 조회한다.")
    void findAllPopular() {
        // given
        List<Theme> expectedThemes = List.of(WOOTECO_THEME(1L), HORROR_THEME(2L));

        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now().minusDays(1);
        BDDMockito.given(themeRepository.findAllByDateBetweenAndOrderByReservationCount(startDate, endDate, 10))
                .willReturn(expectedThemes);

        // when
        List<ThemeResponse> responses = themeService.findAllPopular();

        // then
        ThemeResponse expectedWootecoTheme = ThemeResponse.from(WOOTECO_THEME(1L));
        ThemeResponse expectedHorrorTheme = ThemeResponse.from(HORROR_THEME(2L));
        assertThat(responses).hasSize(2)
                .containsExactly(expectedWootecoTheme, expectedHorrorTheme);
    }
}
