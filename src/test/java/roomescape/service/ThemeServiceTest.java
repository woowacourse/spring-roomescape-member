package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Theme;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {
    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("존재하지 않는 id로 테마를 조회하면 예외가 발생한다")
    void throwException_WhenThemeNotFoundById() {
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> themeService.findById(1L))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.THEME_NOT_FOUND));
    }

    @Test
    @DisplayName("id로 테마를 삭제한다")
    void deleteThemeById() {
        Theme theme = new Theme(1L, "escape1", "방탈출1", "http://example.com/img1.jpg");
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
        when(reservationRepository.existsByThemeId(1L)).thenReturn(false);

        assertThatCode(() -> themeService.deleteTheme(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 테마는 삭제할 수 없다")
    void throwException_WhenDeleteThemeNotFound() {
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.THEME_NOT_FOUND));
    }

    @Test
    @DisplayName("예약이 존재하는 테마는 삭제할 수 없다")
    void throwException_WhenThemeHasReservation() {
        Theme theme = new Theme(1L, "escape1", "방탈출1", "http://example.com/img1.jpg");
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
        when(reservationRepository.existsByThemeId(1L)).thenReturn(true);

        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.THEME_IN_USE));
    }
}
