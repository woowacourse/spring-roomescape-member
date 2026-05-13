package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationLocalDate;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.time.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final ReservationTime SAVED_TIME = new ReservationTime(1L, "12:30");
    private static final Theme SAVED_THEME = new Theme(1L, new ThemeName("name"), "description",
        ThemeImageUrl.defaultImageUrl());

    @Mock
    private ThemeRepository themeRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationTimeRepository timeRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation() {
        return new Reservation(
            1L,
            new MemberName("name"),
            new ReservationLocalDate(LocalDate.now().minusDays(1L)),
            SAVED_TIME, SAVED_THEME);
    }

    @Test
    void 예약을_삭제한다() {
        // given
        when(reservationRepository.existsById(anyLong()))
            .thenReturn(false);

        // when & then
        Long id = reservation().getId();
        assertThatCode(() -> reservationService.deleteReservation(id))
            .doesNotThrowAnyException();

        verify(reservationRepository, times(1)).existsById(id);
        verify(reservationRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 없는_예약을_삭제하는_경우_예외가_발생한다() {
        // given
        when(reservationRepository.existsById(anyLong()))
            .thenReturn(true);

        // when & then
        Long id = reservation().getId();
        assertThatThrownBy(() -> reservationService.deleteReservation(id))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.RESERVATION_NOT_FOUND.getMessage());

        verify(reservationRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 날짜와_테마아이디로_예약가능한_시간을_조회한다() {
        // given
        LocalDate date = LocalDate.now().minusDays(1L);
        ReservationTime availableTime2 = new ReservationTime(2L, "14:30");

        when(themeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_THEME));
        when(timeRepository.findByDateAndThemeId(any(), anyLong()))
            .thenReturn(List.of(SAVED_TIME, availableTime2));

        // when
        List<ReservationTime> availableTimes = reservationService
            .getAvailableTimes(date, SAVED_THEME.getId());

        // then
        assertThat(availableTimes).containsExactlyInAnyOrder(SAVED_TIME, availableTime2);

        verify(themeRepository, times(1)).findById(anyLong());
        verify(timeRepository, times(1)).findByDateAndThemeId(any(), anyLong());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 동일한_테마에서_이미_예약된_날짜와_시간으로_추가하면_예외가_발생한다() {
        // given
        when(timeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_TIME));
        when(themeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_THEME));
        when(timeRepository.findByDateAndThemeId(any(), anyLong()))
            .thenReturn(List.of());

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(requestDtoFrom(reservation())))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.DUPLICATED_RESERVATION.getMessage());

        verify(timeRepository, times(1)).findById(anyLong());
        verify(themeRepository, times(1)).findById(anyLong());
        verify(timeRepository, times(1)).findByDateAndThemeId(any(), anyLong());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 날짜와_시간이_같더라도_테마가_다르면_예약할_수_있다() {
        // given
        Theme otherTheme = new Theme(2L, new ThemeName("name"), "description", ThemeImageUrl.defaultImageUrl());
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation reservation = new Reservation("name", tomorrow, SAVED_TIME, otherTheme);

        when(timeRepository.findById(SAVED_TIME.getId()))
            .thenReturn(Optional.of(SAVED_TIME));
        when(themeRepository.findById(otherTheme.getId()))
            .thenReturn(Optional.of(otherTheme));
        when(timeRepository.findByDateAndThemeId(any(LocalDate.class), eq(otherTheme.getId())))
            .thenReturn(List.of(SAVED_TIME));

        // when & then
        assertThatCode(() -> reservationService.addReservation(requestDtoFrom(reservation)))
            .doesNotThrowAnyException();

        verify(timeRepository, times(1)).findById(anyLong());
        verify(themeRepository, times(1)).findById(anyLong());
        verify(timeRepository, times(1)).findByDateAndThemeId(any(), anyLong());
        verify(reservationRepository, times(1)).createReservation(any());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 예약이_존재하는_시간을_삭제하는_경우_예외가_발생한다() {
        // given
        when(reservationRepository.existsByTimeIdAndDateOnOrAfter(anyLong(), any()))
            .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservationTime(SAVED_TIME.getId()))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.TIME_HAS_RESERVATIONS.getMessage());

        verify(reservationRepository, times(1)).existsByTimeIdAndDateOnOrAfter(anyLong(), any());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    private ReservationRequest requestDtoFrom(Reservation reservation) {
        return new ReservationRequest(
            reservation.getName().value(),
            reservation.getDateValue(),
            reservation.getTime().getId(),
            reservation.getThemeId());
    }
}
