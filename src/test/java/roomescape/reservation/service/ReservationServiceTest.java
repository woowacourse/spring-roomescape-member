package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.*;
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

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-05-01T00:00:00Z"), ZoneOffset.UTC);
        reservationService = new ReservationService(
                reservationRepository, reservationTimeRepository, themeRepository, fixedClock
        );
    }

    @Test
    void 예약_목록을_조회하면_Repository_findAll_결과를_반환한다() {
        List<Reservation> reservations = List.of(
                new Reservation(
                        1L,
                        "브라운",
                        LocalDate.of(2026, 5, 10),
                        new ReservationTime(1L, LocalTime.of(10, 0)),
                        new Theme(1L, "공포방", "무서운방입니다.", "image-url")
                )
        );
        when(reservationRepository.findAll(0, 10)).thenReturn(reservations);

        List<Reservation> result = reservationService.findReservations(0, 10);

        verify(reservationRepository).findAll(0, 10);
        assertThat(result).isSameAs(reservations);
    }

    @Test
    void 사용자_예약_목록을_조회하면_Repository_findByName_결과를_반환한다() {
        List<Reservation> reservations = List.of(
                new Reservation(
                        1L,
                        "브라운",
                        LocalDate.of(2026, 5, 10),
                        new ReservationTime(1L, LocalTime.of(10, 0)),
                        new Theme(1L, "공포방", "무서운방입니다.", "image-url")
                )
        );
        when(reservationRepository.findByName("브라운", 1, 5)).thenReturn(reservations);

        List<Reservation> result = reservationService.findUserReservations("브라운", 1, 5);

        verify(reservationRepository).findByName("브라운", 1, 5);
        assertThat(result).isSameAs(reservations);
    }

    @Test
    void 예약을_생성하면_시간과_테마를_조회하고_예약을_저장한_뒤_예약을_반환한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(2L, "공포방", "무서운방입니다.", "image-url");
        Reservation reservation = new Reservation(1L, "어셔", LocalDate.of(2026, 5, 10), time, theme);

        when(reservationTimeRepository.findById(any())).thenReturn(Optional.of(time));
        when(themeRepository.findById(any())).thenReturn(Optional.of(theme));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.createReservation("어셔", LocalDate.of(2026, 5, 10), 1L, 2L);

        verify(reservationTimeRepository).findById(any());
        verify(themeRepository).findById(any());
        verify(reservationRepository).save(any(Reservation.class));
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("어셔");
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2026, 5, 10));
        assertThat(result.getTime().getId()).isEqualTo(1L);
        assertThat(result.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(result.getTheme().getId()).isEqualTo(2L);
        assertThat(result.getTheme().getName()).isEqualTo("공포방");
    }

    @Test
    void 예약_날짜가_과거인_경우_예외가_발생한다() {
        ReservationTime newTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");

        when(reservationTimeRepository.findById(any())).thenReturn(Optional.of(newTime));
        when(themeRepository.findById(any())).thenReturn(Optional.of(theme));

        assertThatThrownBy(() -> reservationService.createReservation("레서", LocalDate.of(2026, 4, 1), 1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.PAST_RESERVATION);
    }

    @Test
    void 이미_예약된_예약_시간인_경우_예외가_발생한다() {
        ReservationTime newTime = new ReservationTime(2L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "공포방", "무서운방입니다.", "image-url");
        when(reservationTimeRepository.findById(any())).thenReturn(Optional.of(newTime));
        when(themeRepository.findById(any())).thenReturn(Optional.of(theme));
        when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(), any(), any())).thenReturn(true);

        assertThatThrownBy(() -> reservationService.createReservation("레서", LocalDate.of(2026, 5, 13), 1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.DUPLICATE_RESERVATION);
    }

    @Test
    void 존재하지_않는_시간으로_예약을_생성하면_예외가_발생하고_예약을_저장하지_않는다() {
        when(reservationTimeRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.createReservation(
                "브라운", LocalDate.of(2026, 5, 10), 999L, 2L))
                .isInstanceOf(BusinessException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.RESERVATION_TIME_NOT_FOUND);

        verify(themeRepository, never()).findById(2L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void 예약_삭제를_요청하면_Repository_deleteById에_id를_전달한다() {
        when(reservationRepository.deleteById(any())).thenReturn(1);

        reservationService.deleteReservation(7L);

        verify(reservationRepository).deleteById(7L);
    }

    @Test
    void 존재하지_않는_예약을_삭제해도_예외가_발생하지_않는다() {
        when(reservationRepository.deleteById(any())).thenReturn(0);

        reservationService.deleteReservation(999L);

        verify(reservationRepository).deleteById(999L);
    }
}
