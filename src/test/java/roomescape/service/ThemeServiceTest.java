package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.theme.ThemeDao;
import roomescape.domain.Theme;
import roomescape.exception.ReservationExistException;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    ThemeDao themeDao;

    @InjectMocks
    ThemeService themeService;

    @DisplayName("존재하지 않는 id로 테마를 찾을 경우 예외가 발생한다.")
    @Test
    void findByIdThrowExceptionTest() {

        // given
        when(themeDao.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> themeService.findById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("테마가 존재하지 않습니다.");
    }

    @DisplayName("테마가 사용되지 않는 경우 삭제한다.")
    @Test
    void deleteIfNoReservationTest() {

        // given
        when(themeDao.findById(1L)).thenReturn(Optional.of(new Theme(1L, "test", "test", "test")));
        when(themeDao.deleteIfNoReservation(1L)).thenReturn(true);

        // when & then
        assertThatCode(() -> themeService.deleteIfNoReservation(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("테마가 사용되는 경우 삭제하지 않고 예외가 발생한다.")
    @Test
    void deleteIfNoReservationThrowExceptionTest() {

        // given
        when(themeDao.findById(1L)).thenReturn(Optional.of(new Theme(1L, "test", "test", "test")));
        when(themeDao.deleteIfNoReservation(1L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> themeService.deleteIfNoReservation(1L))
                .isInstanceOf(ReservationExistException.class)
                .hasMessage("이 테마에 대한 예약이 존재합니다.");
    }
}
