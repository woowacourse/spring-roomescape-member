package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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
    void 날짜와_테마아이디로_예약가능한_시간을_조회한다() {
        // given
        ReservationTime availableTime1 = new ReservationTime(1L, "12:30");
        ReservationTime availableTime2 = new ReservationTime(2L, "14:30");
        Reservation reservation = reservation();

        when(themeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_THEME));
        when(timeRepository.findByDateAndThemeId(any(), anyLong()))
            .thenReturn(List.of(availableTime1, availableTime2));

        // when
        List<ReservationTime> availableTimes = reservationService
            .getAvailableTimes(reservation.getDateValue(), reservation.getThemeId());

        // then
        assertThat(availableTimes).containsExactlyInAnyOrder(availableTime1, availableTime2);
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
        assertThatThrownBy(() -> reservationService.addReservation(requestDtoFrom(reservation())))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("이미 예약된");
    }

    @Test
    void 날짜와_시간이_같더라도_테마가_다르면_예약할_수_있다() {
        // given
        Theme otherTheme = new Theme(2L, new ThemeName("테마2"), "테마2입니다.", ThemeImageUrl.defaultImageUrl());
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation reservation = new Reservation("이름", tomorrow, SAVED_TIME, otherTheme);

        when(timeRepository.findById(SAVED_TIME.getId()))
            .thenReturn(Optional.of(SAVED_TIME));
        when(themeRepository.findById(otherTheme.getId()))
            .thenReturn(Optional.of(otherTheme));
        when(timeRepository.findByDateAndThemeId(any(LocalDate.class), eq(otherTheme.getId())))
            .thenReturn(List.of(SAVED_TIME));

        // when & then
        assertThatCode(() -> reservationService.addReservation(requestDtoFrom(reservation)))
            .doesNotThrowAnyException();
        verify(reservationRepository).createReservation(any());
    }

    @Test
    void 예약이_존재하는_시간을_삭제하는_경우_예외가_발생한다() {
        // given
        when(reservationRepository.existsByTimeId(anyLong()))
            .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservationTime(SAVED_TIME.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("예약이 존재하는");
    }

    private ReservationRequest requestDtoFrom(Reservation reservation) {
        return new ReservationRequest(
            reservation.getName().value(),
            reservation.getDateValue(),
            reservation.getTime().getId(),
            reservation.getThemeId());
    }
}
