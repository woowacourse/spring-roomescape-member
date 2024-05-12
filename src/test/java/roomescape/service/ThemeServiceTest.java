package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private ThemeDao themeDao;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("새로운 테마를 저장할 수 있다")
    void should_SaveNewTheme() {
        //given
        ThemeRequest request = new ThemeRequest("테마", "테마입니다", "123");
        Theme response = new Theme(1L, request.name(), request.description(), request.thumbnail());
        Mockito.when(themeDao.save(any(Theme.class)))
                .thenReturn(response);

        //when
        ThemeResponse savedTheme = themeService.save(request);

        //then
        assertAll(
                () -> verify(themeDao, times(1)).save(any(Theme.class)),
                () -> assertThat(savedTheme.id()).isEqualTo(response.getId())
        );
    }

    @Test
    @DisplayName("모든 테마를 가져올 수 있다")
    void should_FindAllThemes() {
        //given
        Theme theme = new Theme(1L, "테마", "테마입니다.", "123");
        Mockito.when(themeDao.getAll())
                .thenReturn(List.of(theme));

        //when
        List<ThemeResponse> themes = themeService.getAll();

        //then
        assertAll(
                () -> verify(themeDao, times(1)).getAll(),
                () -> assertThat(themes).hasSize(1),
                () -> assertThat(themes.get(0).id()).isEqualTo(theme.getId())
        );
    }

    @Test
    void findThemeRanking() {
        //given
        Theme dummyTheme1 = new Theme(1L, "테마1", "테마1입니다.", "123");
        Theme dummyTheme2 = new Theme(2L, "테마2", "테마2입니다.", "456");

        Mockito.when(reservationDao.findRanking(any(LocalDate.class), any(LocalDate.class), anyInt()))
                .thenReturn(List.of(1L, 2L));
        Mockito.when(themeDao.findById(anyLong()))
                .thenReturn(Optional.of(dummyTheme1))
                .thenReturn(Optional.of(dummyTheme2));

        //when
        List<ThemeResponse> themeRanking = themeService.findThemeRanking();

        //given
        assertAll(
                () -> verify(reservationDao, times(1))
                        .findRanking(any(LocalDate.class), any(LocalDate.class), anyInt()),
                () -> assertThat(themeRanking.get(0).id()).isEqualTo(dummyTheme1.getId()),
                () -> assertThat(themeRanking.get(1).id()).isEqualTo(dummyTheme2.getId())
        );
    }

    @Test
    @DisplayName("성공 : 테마를 사용중인 예약이 없고 테마가 db안에 존재하면 삭제에 성공한다.")
    void should_SuccessDelete_When_NoOtherReservationUsingTheme_And_ThemeExistInDb() {
        //given
        Theme dummy = new Theme(1L, "", "", "");
        ArgumentCaptor<Long> valueCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.when(reservationDao.findByThemeId(anyLong()))
                .thenReturn(List.of());
        Mockito.when(themeDao.findById(anyLong()))
                .thenReturn(Optional.of(dummy));
        Mockito.doNothing()
                .when(themeDao).delete(valueCaptor.capture());

        //when
        themeService.delete(1L);

        //then
        Mockito.verify(themeDao, times(1)).delete(anyLong());
        assertThat(valueCaptor.getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("실패 : 테마를 사용중인 예약이 있다면 삭제에 실패한다.")
    void should_ThrowIllegalArgumentException_When_HasThemeUsingReservation() {
        //given
        List<Reservation> dummyReservations = List.of(Mockito.mock(Reservation.class));
        Mockito.when(reservationDao.findByThemeId(anyLong()))
                .thenReturn(dummyReservations);
        Mockito.when(themeDao.findById(anyLong()))
                .thenReturn(Optional.of(Mockito.mock(Theme.class)));

        //when - then
        assertThatThrownBy(() -> themeService.delete(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패 : 테마가 db에 존재하지 않는다면 삭제에 실패한다.")
    void should_NoSuchElementException_When_HasThemeUsingReservation() {
        //given
        Mockito.when(themeDao.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        //when - then
        assertThatThrownBy(() -> themeService.delete(1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}