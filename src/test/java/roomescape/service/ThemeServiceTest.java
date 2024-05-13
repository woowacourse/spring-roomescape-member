package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.domain.exception.IllegalRequestArgumentException;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeResponse;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {
    private final Theme theme = new Theme(1L, "테마1", "설명1", "https://image.jpg");
    @Mock
    ThemeDao themeDao;
    @Mock
    ReservationDao reservationDao;
    @InjectMocks
    ThemeService themeService;

    @DisplayName("시간 목록을 읽을 수 있다.")
    @Test
    void readThemes() {
        List<Theme> times = List.of(theme);

        lenient().when(themeDao.readThemes()).thenReturn(times);

        List<ThemeResponse> expected = List.of(ThemeResponse.from(theme));
        assertThat(themeService.readThemes()).isEqualTo(expected);
    }

    @DisplayName("인기 테마 목록을 랭킹 순서 대로 읽을 수 있다.")
    @Test
    void readPopularThemes() {
        List<Theme> times = List.of(theme);

        lenient().when(themeDao.readThemesRankingOfReservation(any(String.class), any(String.class))).thenReturn(times);

        List<ThemeResponse> expected = List.of(ThemeResponse.from(theme));
        assertThat(themeService.readPopularThemes()).isEqualTo(expected);
    }

    @DisplayName("테마를 추가할 수 있다.")
    @Test
    void createTheme() {
        lenient().when(themeDao.createTheme(any(Theme.class))).thenReturn(theme);

        ThemeCreateRequest request = new ThemeCreateRequest("테마1", "설명1", "https://image.jpg");
        assertThatCode(() -> themeService.createTheme(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("테마를 삭제할 수 있다.")
    @Test
    void deleteTime() {
        lenient().when(reservationDao.existsReservationByThemeId(any(Long.class)))
                .thenReturn(false);

        assertThatCode(() -> themeService.deleteTheme(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약이 있는 시간인 경우, 삭제하려고 하면 예외를 던진다.")
    @Test
    void deleteTime_whenExistsReservation() {
        lenient().when(reservationDao.existsReservationByThemeId(any(Long.class)))
                .thenReturn(true);

        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(IllegalRequestArgumentException.class)
                .hasMessage("해당 테마를 사용하는 예약이 존재합니다.");
    }
}
