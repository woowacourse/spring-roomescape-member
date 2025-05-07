package roomescape.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.business.model.entity.Theme;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ThemeRepository;
import roomescape.exception.business.ConnectedReservationExistException;
import roomescape.exception.business.ThemeNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("테마를 추가하고 반환한다")
    void addAndGet_ReturnsTheme() {
        // given
        String name = "주홍색 연구";
        String description = "셜록 홈즈의 첫 번째 사건";
        String thumbnail = "thumbnail.jpg";

        // when
        Theme result = themeService.addAndGet(name, description, thumbnail);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getThumbnail()).isEqualTo(thumbnail);
        verify(themeRepository).save(any(Theme.class));
    }

    @Test
    @DisplayName("모든 테마를 조회할 수 있다")
    void getAll_ReturnsAllThemes() {
        // given
        List<Theme> expectedThemes = Arrays.asList(
                Theme.afterSave("theme-id-1", "Theme One", "Description One", "thumbnail1.jpg"),
                Theme.afterSave("theme-id-2", "Theme Two", "Description Two", "thumbnail2.jpg")
        );

        when(themeRepository.findAll()).thenReturn(expectedThemes);

        // when
        List<Theme> result = themeService.getAll();

        // then
        assertThat(result).isEqualTo(expectedThemes);
        verify(themeRepository).findAll();
    }

    @Test
    @DisplayName("인기 테마를 조회할 수 있다")
    void getPopular_ReturnsPopularThemes() {
        // given
        int size = 3;
        List<Theme> expectedThemes = Arrays.asList(
                Theme.afterSave("theme-id-1", "Popular Theme One", "Description One", "thumbnail1.jpg"),
                Theme.afterSave("theme-id-2", "Popular Theme Two", "Description Two", "thumbnail2.jpg"),
                Theme.afterSave("theme-id-3", "Popular Theme Three", "Description Three", "thumbnail3.jpg")
        );

        when(themeRepository.findPopularThemes(any(LocalDate.class), any(LocalDate.class), eq(size)))
                .thenReturn(expectedThemes);

        // when
        List<Theme> result = themeService.getPopular(size);

        // then
        assertThat(result).isEqualTo(expectedThemes);
        verify(themeRepository).findPopularThemes(any(LocalDate.class), any(LocalDate.class), eq(size));
    }

    @Test
    @DisplayName("테마를 삭제할 수 있다")
    void delete_ExistingTheme_DeletesTheme() {
        // given
        String themeId = "theme-id";

        when(reservationRepository.existByThemeId(themeId)).thenReturn(false);
        when(themeRepository.existById(themeId)).thenReturn(true);

        // when
        themeService.delete(themeId);

        // then
        verify(reservationRepository).existByThemeId(themeId);
        verify(themeRepository).existById(themeId);
        verify(themeRepository).deleteById(themeId);
    }

    @Test
    @DisplayName("존재하지 않는 테마 삭제 시 예외가 발생한다")
    void delete_NonExistingTheme_ThrowsException() {
        // given
        String themeId = "non-existing-id";

        when(reservationRepository.existByThemeId(themeId)).thenReturn(false);
        when(themeRepository.existById(themeId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> themeService.delete(themeId))
                .isInstanceOf(ThemeNotFoundException.class);

        verify(reservationRepository).existByThemeId(themeId);
        verify(themeRepository).existById(themeId);
        verify(themeRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("예약이 연결된 테마 삭제 시 예외가 발생한다")
    void delete_ThemeWithReservations_ThrowsException() {
        // given
        String themeId = "theme-with-reservations";

        when(reservationRepository.existByThemeId(themeId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> themeService.delete(themeId))
                .isInstanceOf(ConnectedReservationExistException.class);

        verify(reservationRepository).existByThemeId(themeId);
        verify(themeRepository, never()).existById(anyString());
        verify(themeRepository, never()).deleteById(anyString());
    }
}
