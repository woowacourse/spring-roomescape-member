package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.service.dto.ThemeRequest;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ThemeService themeService;

    @DisplayName("테마 리스트를 조회할 땐 findAllThemes이 호출되어야 한다.")
    @Test
    void find_all_reservation_times() {
        Theme theme1 = new Theme(1L, "안돌", "안녕하세요", "hi.jpg");
        Theme theme2 = new Theme(2L, "러너덕", "반가워요!", "hi.jpg");
        List<Theme> themes = List.of(theme1, theme2);
        given(themeRepository.findAllThemes()).willReturn(themes);

        themeService.findAllThemes();
        verify(themeRepository, times(1)).findAllThemes();
    }

    @DisplayName("테마를 정상적으로 저장할 땐 insertTheme이 호출되어야 한다.")
    @Test
    void create_reservation_time_test() {
        ThemeRequest requestDto = new ThemeRequest("재즈", "프로그래밍", "좋아.png");
        Theme Theme = new Theme(1L, "재즈", "프로그래밍", "좋아.png");
        given(themeRepository.insertTheme(requestDto.toTheme())).willReturn(Theme);

        themeService.createTheme(requestDto);
        verify(themeRepository, times(1)).insertTheme(requestDto.toTheme());
    }

    @DisplayName("예약이 존재하는 테마를 삭제하려고 하면 예외가 발생한다.")
    @Test
    void throw_exception_when_exist_reservation_delete() {
        long themeId = 1L;
        given(themeRepository.isExistThemeOf(themeId)).willReturn(true);
        given(reservationRepository.hasReservationOfThemeId(themeId)).willReturn(true);

        assertThatThrownBy(() -> themeService.deleteTheme(themeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마에 예약이 있어 삭제할 수 없습니다.");

        verify(themeRepository, never()).deleteThemeById(themeId);
    }

    @DisplayName("존재하지 않는 아이디를 삭제하려고 하면 예외가 발생한다.")
    @Test
    void throw_exception_when_not_exist_id_delete() {
        long themeId = 1L;
        given(themeRepository.isExistThemeOf(themeId)).willReturn(false);

        assertThatThrownBy(() -> themeService.deleteTheme(themeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 아이디입니다.");

        verify(themeRepository, never()).deleteThemeById(themeId);
    }

    @DisplayName("예약이 존재하지 않는 테마를 삭제할 수 있다.")
    @Test
    void delete_reservation_time() {
        long themeId = 1L;
        given(themeRepository.isExistThemeOf(themeId)).willReturn(true);
        given(reservationRepository.hasReservationOfThemeId(themeId)).willReturn(false);

        themeService.deleteTheme(themeId);
        verify(themeRepository, times(1)).deleteThemeById(themeId);
    }
}
