package roomescape.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import roomescape.dto.reservation.ReservationRequestDto;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.time.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ThemeRepository themeRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationTimeRepository timeRepository;

    @InjectMocks
    private ReservationService reservationService;

    private static final ReservationTime SAVED_TIME = new ReservationTime(1L, "12:30");
    private static final Theme SAVED_THEME = new Theme(1L, "name", "description", "image-url");
    private static final Reservation RESERVATION = new Reservation(1L, "name", "2026-05-05", SAVED_TIME, SAVED_THEME);

    @Test
    void 날짜와_테마아이디로_예약가능한_시간을_조회한다() {
        // given
        ReservationTime availableTime1 = new ReservationTime(1L, "12:30");
        ReservationTime availableTime2 = new ReservationTime(2L, "14:30");

        when(themeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_THEME));
        when(timeRepository.findByDateAndThemeId(any(), anyLong()))
            .thenReturn(List.of(availableTime1, availableTime2));

        // when
        List<ReservationTime> availableTimes = reservationService
            .getAvailableTimes(RESERVATION.getDate(), RESERVATION.getThemeId());

        // then
        assertThat(availableTimes).hasSize(2);
        assertThat(availableTimes).extracting(ReservationTime::getId)
            .anySatisfy(id -> assertThat(id).isEqualTo(1L))
            .anySatisfy(id -> assertThat(id).isEqualTo(2L));

    }

    @Test
    void 동일한_테마에서_이미_예약된_시간을_선택하면_예외가_발생한다() {
        // given
        when(timeRepository.findById(SAVED_TIME.getId()))
            .thenReturn(Optional.of(SAVED_TIME));
        when(themeRepository.findById(SAVED_THEME.getId()))
            .thenReturn(Optional.of(SAVED_THEME));

        when(timeRepository.findByDateAndThemeId(any(), anyLong()))
            .thenReturn(List.of());

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(requestDtoFrom(RESERVATION)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("이미 예약된");
    }

    @Test
    void 날짜와_시간이_같더라도_테마가_다르면_예약할_수_있다() {
        // given
        Theme otherTheme = new Theme(2L, "테마2", "테마2입니다.", "url");
        Reservation reservation = new Reservation("이름", "2026-05-05", SAVED_TIME, otherTheme);

        when(timeRepository.findById(SAVED_TIME.getId()))
            .thenReturn(Optional.of(SAVED_TIME));
        when(themeRepository.findById(otherTheme.getId()))
            .thenReturn(Optional.of(otherTheme));

        when(timeRepository.findByDateAndThemeId(any(LocalDate.class), eq(otherTheme.getId())))
            .thenReturn(List.of(SAVED_TIME));

        // when & then
        assertThatCode(() -> reservationService.addReservation(requestDtoFrom(reservation)))
            .doesNotThrowAnyException();
        verify(reservationRepository, times(1)).createReservation(any());
    }

    private ReservationRequestDto requestDtoFrom(Reservation reservation) {
        return new ReservationRequestDto(reservation.getName().value(), reservation.getDate(),
            reservation.getTime().getId(), reservation.getThemeId());
    }
}