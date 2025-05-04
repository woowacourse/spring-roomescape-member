package roomescape.unit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static roomescape.common.Constant.FIXED_CLOCK;

import java.lang.reflect.Field;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.common.Constant;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.controller.request.CreateThemeRequest;
import roomescape.theme.controller.response.ThemeResponse;
import roomescape.theme.domain.LastWeekRange;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.ThemeService;

public class ThemeServiceTest {

    private ThemeRepository themeRepository;
    private ReservationRepository reservationRepository;
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        themeRepository = Mockito.mock(ThemeRepository.class);
        reservationRepository = Mockito.mock(ReservationRepository.class);
        themeService = new ThemeService(themeRepository, reservationRepository, FIXED_CLOCK);
    }

    @Test
    void 테마를_생성할_수_있다() {
        // given
        CreateThemeRequest request = new CreateThemeRequest("공포", "무섭다", "thumb.jpg");
        Theme theme = new Theme(1L, "공포", "무섭다", "thumb.jpg");
        Mockito.when(themeRepository.save(request.name(), request.description(), request.thumbnail()))
                .thenReturn(theme);

        // when
        ThemeResponse response = themeService.createTheme(request);

        // then
        assertThat(response.name()).isEqualTo("공포");
        assertThat(response.description()).isEqualTo("무섭다");
        assertThat(response.thumbnail()).isEqualTo("thumb.jpg");
    }

    @Test
    void 모든_테마를_조회할_수_있다() {
        // given
        List<Theme> themes = List.of(
                new Theme(1L, "공포", "무섭다", "thumb.jpg"),
                new Theme(2L, "로맨스", "달달하다", "love.jpg")
        );
        Mockito.when(themeRepository.findAll()).thenReturn(themes);

        // when
        List<ThemeResponse> result = themeService.getAllThemes();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 예약이_없는_테마는_삭제할_수_있다() {
        // given
        Long id = 1L;
        Theme theme = new Theme(id, "공포", "무섭다", "thumb.jpg");
        Mockito.when(reservationRepository.existReservationByThemeId(id)).thenReturn(false);
        Mockito.when(themeRepository.findById(id)).thenReturn(Optional.of(theme));

        // when & then
        assertThatCode(() -> themeService.deleteThemeById(id))
                .doesNotThrowAnyException();
        Mockito.verify(themeRepository).deleteById(id);
    }

    @Test
    void 예약이_있는_테마는_삭제할_수_없다() {
        // given
        Long id = 1L;
        Mockito.when(reservationRepository.existReservationByThemeId(id)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> themeService.deleteThemeById(id))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 존재하지_않는_테마는_삭제할_수_없다() {
        // given
        Long id = 999L;
        Mockito.when(reservationRepository.existReservationByThemeId(id)).thenReturn(false);
        Mockito.when(themeRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> themeService.deleteThemeById(id))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 최근_일주일_인기_테마를_조회할_수_있다() {
        // given
        List<Theme> themes = List.of(
                new Theme(1L, "공포", "무섭다", "thumb.jpg"),
                new Theme(2L, "로맨스", "달달하다", "love.jpg")
        );
        LastWeekRange lastWeekRange = new LastWeekRange(LocalDate.now(FIXED_CLOCK));
        Mockito.when(themeRepository.findPopularThemeDuringAWeek(eq(10L), any(LastWeekRange.class)))
                .thenReturn(themes);

        // when
        List<ThemeResponse> result = themeService.getWeeklyPopularThemes();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("공포");
    }
}
