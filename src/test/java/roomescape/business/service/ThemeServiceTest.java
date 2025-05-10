package roomescape.business.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.business.model.entity.Theme;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ThemeRepository;
import roomescape.exception.business.NotFoundException;
import roomescape.exception.business.RelatedEntityExistException;

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
    private ThemeService sut;

    @Test
    void 테마를_추가하고_반환한다() {
        // given
        String name = "주홍색 연구";
        String description = "셜록 홈즈의 첫 번째 사건";
        String thumbnail = "thumbnail.jpg";

        // when
        Theme result = sut.addAndGet(name, description, thumbnail);

        // then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(name);
        assertThat(result.description()).isEqualTo(description);
        assertThat(result.thumbnail()).isEqualTo(thumbnail);
        verify(themeRepository).save(any(Theme.class));
    }

    @Test
    void 모든_테마를_조회할_수_있다() {
        // given
        List<Theme> expectedThemes = Arrays.asList(
                Theme.restore("theme-id-1", "Theme One", "Description One", "thumbnail1.jpg"),
                Theme.restore("theme-id-2", "Theme Two", "Description Two", "thumbnail2.jpg")
        );

        when(themeRepository.findAll()).thenReturn(expectedThemes);

        // when
        List<Theme> result = sut.getAll();

        // then
        assertThat(result).isEqualTo(expectedThemes);
        verify(themeRepository).findAll();
    }

    @Test
    void 인기_테마를_조회할_수_있다() {
        // given
        int size = 3;
        List<Theme> expectedThemes = Arrays.asList(
                Theme.restore("theme-id-1", "Popular Theme One", "Description One", "thumbnail1.jpg"),
                Theme.restore("theme-id-2", "Popular Theme Two", "Description Two", "thumbnail2.jpg"),
                Theme.restore("theme-id-3", "Popular Theme Three", "Description Three", "thumbnail3.jpg")
        );

        when(themeRepository.findPopularThemes(any(LocalDate.class), any(LocalDate.class), eq(size)))
                .thenReturn(expectedThemes);

        // when
        List<Theme> result = sut.getPopular(size);

        // then
        assertThat(result).isEqualTo(expectedThemes);
        verify(themeRepository).findPopularThemes(any(LocalDate.class), any(LocalDate.class), eq(size));
    }

    @Test
    void 테마를_삭제할_수_있다() {
        // given
        String themeId = "theme-id";

        when(reservationRepository.existByThemeId(themeId)).thenReturn(false);
        when(themeRepository.existById(themeId)).thenReturn(true);

        // when
        sut.delete(themeId);

        // then
        verify(reservationRepository).existByThemeId(themeId);
        verify(themeRepository).existById(themeId);
        verify(themeRepository).deleteById(themeId);
    }

    @Test
    void 존재하지_않는_테마_삭제_시_예외가_발생한다() {
        // given
        String themeId = "non-existing-id";

        when(reservationRepository.existByThemeId(themeId)).thenReturn(false);
        when(themeRepository.existById(themeId)).thenReturn(false);

        // when, then
        assertThatThrownBy(() -> sut.delete(themeId))
                .isInstanceOf(NotFoundException.class);

        verify(reservationRepository).existByThemeId(themeId);
        verify(themeRepository).existById(themeId);
        verify(themeRepository, never()).deleteById(anyString());
    }

    @Test
    void 예약이_연결된_테마_삭제_시_예외가_발생한다() {
        // given
        String themeId = "theme-with-reservations";

        when(reservationRepository.existByThemeId(themeId)).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> sut.delete(themeId))
                .isInstanceOf(RelatedEntityExistException.class);

        verify(reservationRepository).existByThemeId(themeId);
        verify(themeRepository, never()).existById(anyString());
        verify(themeRepository, never()).deleteById(anyString());
    }
}
