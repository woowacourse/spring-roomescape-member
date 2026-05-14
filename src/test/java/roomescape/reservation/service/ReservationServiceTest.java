package roomescape.reservation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.global.exception.BusinessException;
import roomescape.global.exception.ErrorCode;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 예약_목록을_조회하면_Repository_findAllWithTime_결과를_반환한다() {
        List<Reservation> reservations = List.of(
                new Reservation(
                        1L,
                        "브라운",
                        LocalDate.now().plusDays(1),
                        new ReservationTime(1L, LocalTime.of(10, 0)),
                        new Theme(1L, "공포방", "무서운방입니다.", "image-url")
                )
        );
        when(reservationRepository.findAllWithTime()).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservations();

        verify(reservationRepository).findAllWithTime();
        assertThat(result).isSameAs(reservations);
    }

    @Test
    void 예약을_생성하면_시간과_테마를_조회하고_예약을_저장한_뒤_예약을_반환한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(2L, "공포방", "무서운방입니다.", "image-url");
        Reservation reservation = new Reservation(1L, "어셔", LocalDate.now().plusDays(1), time, theme);

        when(reservationTimeRepository.findById(any())).thenReturn(Optional.of(time));
        when(themeRepository.findById(any())).thenReturn(Optional.of(theme));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.createReservation("어셔", LocalDate.now().plusDays(1), 1L, 2L);

        verify(reservationTimeRepository).findById(any());
        verify(themeRepository).findById(any());
        verify(reservationRepository).save(any(Reservation.class));
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("어셔");
        assertThat(result.getDate()).isEqualTo(LocalDate.now().plusDays(1));
        assertThat(result.getTime()).isSameAs(time);
        assertThat(result.getTheme()).isSameAs(theme);
    }

    @Test
    void 존재하지_않는_시간으로_예약을_생성하면_예외가_발생하고_예약을_저장하지_않는다() {
        when(reservationTimeRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.createReservation(
                "브라운", LocalDate.of(2026, 5, 10), 999L, 2L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_TIME_NOT_FOUND);

        verify(themeRepository, never()).findById(2L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void 존재하는_예약을_삭제한다() {
        when(reservationRepository.existsById(any())).thenReturn(true);

        reservationService.deleteReservation(1L);

        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_예외가_발생한다() {
        when(reservationRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> reservationService.deleteReservation(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
    }

    @Test
    void 동일한테마_동일한시간대에_예약이_존재할_때_예약_시_예외를_반환한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeRepository.findById(any()))
                .thenReturn(Optional.of(time));
        Theme theme = new Theme(1L, "공포", "무서운 테마", "image");
        when(themeRepository.findById(1L))
                .thenReturn(Optional.of(theme));
        when(reservationRepository.existsByDateAndTimeIdAndThemeId(LocalDate.now().plusDays(1), 1L, 1L))
                .thenReturn(true);

        assertThatThrownBy(() -> reservationService.createReservation(
                "어셔", LocalDate.now().plusDays(1), 1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_DUPLICATE);

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void 과거_날짜로_예약을_생성하면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");
        when(reservationTimeRepository.findById(any())).thenReturn(Optional.of(time));
        when(themeRepository.findById(any())).thenReturn(Optional.of(theme));

        assertThatThrownBy(() -> reservationService.createReservation(
                "어셔", LocalDate.now().minusDays(1), 1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_PAST_DATETIME);

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void 오늘_날짜_지난_시간으로_예약을_생성하면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.MIN);  // 00:00
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        when(reservationTimeRepository.findById(any())).thenReturn(Optional.of(time));
        when(themeRepository.findById(any())).thenReturn(Optional.of(theme));

        assertThatThrownBy(() -> reservationService.createReservation(
                "어셔", LocalDate.now(), 1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_PAST_DATETIME);
    }

    @Test
    void 미래_시간의_예약을_취소하면_성공한다() {
        // given
        Reservation reservation = new Reservation(
                1L, "어셔", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, "공포방", "무서운 방입니다.", "img-url")
        );
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // when
        reservationService.cancelUserReservation(1L, "어셔");

        // then
        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void 과거_시간의_예약을_취소하면_RESERVATION_EXPIRED_예외를_던진다() {
        // given
        Reservation reservation = new Reservation(
                1L, "어셔", LocalDate.now().minusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, "공포방", "무서운 방입니다.", "img-url")
        );
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.cancelUserReservation(1L, "어셔"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_EXPIRED);

        verify(reservationRepository, never()).deleteById(any());
    }

    @Test
    void 존재하지_않는_예약을_취소하면_RESREVATION_NOT_FOUND_예외를_던진다() {
        // given
        when(reservationRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.cancelUserReservation(1L, "어셔"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);

        verify(reservationRepository, never()).deleteById(any());
    }

    @Test
    void 본인이_아닌_예약을_취소하면_RESERVATION_FORBIDDEN_예외를_던진다() {
        // given
        Reservation reservation = new Reservation(
                1L, "어셔", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, "공포방", "무서운 방입니다.", "img-url")
        );
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.cancelUserReservation(1L, "어셔2"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_FORBIDDEN);

        verify(reservationRepository, never()).deleteById(any());
    }

    @Test
    void 날짜와_시간이_지난_예약을_삭제하면_RESERVATION_EXPIRED를_던지고_예외를_발생한다() {
        // given
        Reservation reservation = new Reservation(
                1L, "어셔", LocalDate.now().minusDays(1),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, "공포방", "무서운 방입니다.", "img-url")
        );
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.cancelUserReservation(1L, "어셔"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_EXPIRED);

        verify(reservationRepository, never()).deleteById(1L);
    }

    @Test
    void 오늘_날짜이고_시간이_지난_예약을_삭제하면_RESERVATION_EXPIRED를_던지고_예외를_발생한다() {
        // given
        LocalTime now = LocalTime.now();
        int beforeHour = now.getHour() - 1;
        Reservation reservation = new Reservation(
                1L, "어셔", LocalDate.now(),
                new ReservationTime(1L, LocalTime.of(beforeHour, 0)),
                new Theme(1L, "공포방", "무서운 방입니다.", "img-url")
        );
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.cancelUserReservation(1L, "어셔"))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_EXPIRED);
    }

    @Test
    void 본인의_미래_예약의_날짜와_시간을_변경한다() {
        // given
        ReservationTime oldTime = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime newTime = new ReservationTime(2L, LocalTime.of(13, 0));
        Theme theme = new Theme(1L, "공포방", "공포스럽습니다...", "url-image");
        Reservation reservation = new Reservation(
                1L, "어셔", LocalDate.now().plusDays(1), oldTime, theme);

        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findById(any())).thenReturn(Optional.of(newTime));
        when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(), any(), any())).thenReturn(false);

        // when
        Reservation result = reservationService.updateUserReservation(1L, "어셔", LocalDate.now().plusDays(2), 2L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDate()).isEqualTo(LocalDate.now().plusDays(2));
        assertThat(result.getTime().getId()).isEqualTo(newTime.getId());
        assertThat(result.getTime().getStartAt()).isEqualTo(newTime.getStartAt());
    }

    @Test
    void 존재하지_않는_예약을_변경하면_RESERVATION_NOT_FOUND를_던지고_수정하지_않는다() {
        // given
        when(reservationRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.updateUserReservation(
                1L, "어셔", LocalDate.now().plusDays(1), 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_NOT_FOUND);
    }

    @Test
    void 수정하는_예약이_본인의_예약이_아니라면_RESERVATION_FORBIDDEN을_던지고_수정하지_않는다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "공포스럽습니다...", "url-image");
        Reservation reservation = new Reservation(
                1L, "어셔", LocalDate.now().plusDays(1), time, theme);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.updateUserReservation(1L, "어셔2", LocalDate.now().plusDays(1), 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_FORBIDDEN);
    }

    @Test
    void 수정하는_예약이_지난_시간의_예약이라면_RESERVATION_EXPIRED를_던지고_수정하지_않는다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "공포스럽습니다...", "url-image");
        Reservation reservation = new Reservation(
                1L, "어셔", LocalDate.now().minusDays(1), time, theme);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.updateUserReservation(1L, "어셔", LocalDate.now().plusDays(1), 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_EXPIRED);
    }

    @Test
    void 존재하지_않는_시간으로_예약_변경을_시도하면_RESERVATION_TIME_NOT_FOUND를_던지고_수정하지_않는다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "공포스럽습니다...", "url-image");
        Reservation reservation = new Reservation(
                1L, "어셔", LocalDate.now().plusDays(1), time, theme);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findById(any())).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> reservationService.updateUserReservation(
                1L, "어셔", LocalDate.now(), 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_TIME_NOT_FOUND);
    }

    @Test
    void 과거_시간을_예약_변경을_시도한다면_RESERVATION_PAST_DATETIME을_던지고_수정하지_않는다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "공포스럽습니다...", "url-image");
        Reservation reservation = new Reservation(
                1L, "어셔", LocalDate.now().plusDays(1), time, theme);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findById(any())).thenReturn(Optional.of(time));

        // when
        assertThatThrownBy(() -> reservationService.updateUserReservation(
                1L, "어셔", LocalDate.now().minusDays(1), 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_PAST_DATETIME);
    }

    @Test
    void 변경하려는_일시에_다른_예약이_있다면_RESERVATION_DUPLICATE를_던지고_수정하지_않는다() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포방", "공포스럽습니다...", "url-image");
        Reservation reservation = new Reservation(
                1L, "어셔", LocalDate.now().plusDays(1), time, theme);
        when(reservationRepository.findById(any())).thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findById(any())).thenReturn(Optional.of(time));
        when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(), any(), any())).thenReturn(true);

        assertThatThrownBy(() -> reservationService.updateUserReservation(
                1L, "어셔", LocalDate.now().plusDays(1), 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_DUPLICATE);
    }
}
