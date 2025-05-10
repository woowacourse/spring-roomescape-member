package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.dao.ReservationDao;
import roomescape.theme.dao.ThemeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    ThemeDao themeDao;

    @Mock
    ReservationDao reservationDao;

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
        when(themeDao.findById(1L)).thenReturn(Optional.of(Theme.load(1L, "test", "test", "test")));
        when(reservationDao.findByThemeId(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatCode(() -> themeService.deleteIfNoReservation(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("테마가 사용되는 경우 삭제하지 않고 예외가 발생한다.")
    @Test
    void deleteIfNoReservationThrowExceptionTest() {

        // given
        Theme theme = Theme.load(1L, "test", "test", "test");
        when(themeDao.findById(1L)).thenReturn(Optional.of(theme));
        when(reservationDao.findByThemeId(1L)).thenReturn(Optional.of(
                Reservation.load(1L, "test", LocalDate.now(), ReservationTime.load(1L, LocalTime.of(12, 1)),
                        theme)));

        // when & then
        assertThatThrownBy(() -> themeService.deleteIfNoReservation(1L))
                .isInstanceOf(ReservationExistException.class)
                .hasMessage("이 테마에 대한 예약이 존재합니다.");
    }

    @DisplayName("최근 7일간 가장 인기있었던 상위 10개 테마를 반환한다.")
    @Test
    void findPopularThemesInRecentSevenDaysTest() {

        // given
        Theme themeFirst = Theme.load(1L, "test1", "test", "test");
        Theme themeSecond = Theme.load(2L, "test2", "test", "test");
        Theme themeThird = Theme.load(3L, "test3", "test", "test");
        ReservationTime reservationTime = ReservationTime.load(1L, LocalTime.of(12, 1));

        when(reservationDao.findAll()).thenReturn(
                List.of(Reservation.load(1L, "test", LocalDate.now().minusDays(1), reservationTime, themeFirst),
                        Reservation.load(2L, "test", LocalDate.now().minusDays(1), reservationTime, themeFirst),
                        Reservation.load(3L, "test", LocalDate.now().minusDays(1), reservationTime, themeFirst),
                        Reservation.load(4L, "test", LocalDate.now().minusDays(1), reservationTime, themeSecond),
                        Reservation.load(5L, "test", LocalDate.now().minusDays(1), reservationTime, themeSecond),
                        Reservation.load(6L, "test", LocalDate.now().minusDays(1), reservationTime, themeThird))
        );

        // when
        List<ThemeResponse> popularThemes = themeService.findPopularThemesInRecentSevenDays();

        // then
        assertAll(
                () -> assertThat(popularThemes.get(0).id()).isEqualTo(1L),
                () -> assertThat(popularThemes.get(1).id()).isEqualTo(2L),
                () -> assertThat(popularThemes.get(2).id()).isEqualTo(3L)
        );

    }
}
