package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;
import roomescape.holiday.service.HolidayService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.reservation.service.dto.ReservationUpdateServiceDto;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.TimeService;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TimeService timeService;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private HolidayService holidayService;

    private ReservationServiceImpl reservationService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-05-12T10:00:00Z"), ZoneId.of("UTC"));
        reservationService = new ReservationServiceImpl(
                reservationRepository,
                timeService,
                themeRepository,
                holidayService,
                fixedClock
        );
    }

    @Test
    void 과거_날짜와_시간이면_예외가_발생한다() {
        ReservationSaveServiceDto request = new ReservationSaveServiceDto("브라운", LocalDate.of(2026, 5, 12), 1L, 1L);
        ReservationTime time = new ReservationTime(1L, "09:00", "10:00");

        when(timeService.findById(1L)).thenReturn(time);
        when(themeRepository.existsById(1L)).thenReturn(true);
        when(themeRepository.findById(1L)).thenReturn(new Theme("테마", "설명", "url").withId(1L));

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PAST_RESERVATION_NOT_ALLOWED);

        verify(reservationRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void 이름이_비어있으면_예외가_발생한다() {
        ReservationSaveServiceDto request = new ReservationSaveServiceDto(" ", LocalDate.of(2099, 8, 5), 1L, 1L);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_REQUEST);
    }

    @Test
    void 중복_예약이면_예외가_발생한다() {
        ReservationSaveServiceDto request = new ReservationSaveServiceDto("브라운", LocalDate.of(2099, 8, 5), 1L, 1L);
        ReservationTime time = new ReservationTime(1L, "10:00", "11:00");

        when(timeService.findById(1L)).thenReturn(time);
        when(themeRepository.existsById(1L)).thenReturn(true);
        when(themeRepository.findById(1L)).thenReturn(new Theme("테마", "설명", "url").withId(1L));
        when(holidayService.isHoliday(request.date())).thenReturn(false);
        when(reservationRepository.isDuplicated(1L, time, request.date())).thenReturn(true);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_RESERVATION);

        verify(reservationRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void 소유자가_아니면_취소_시_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, "10:00", "11:00");
        Theme theme = new Theme("테마", "설명", "url").withId(1L);
        Reservation reservation = new Reservation("브라운", LocalDate.of(2099, 8, 5), time, theme).withId(1L);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.cancel(1L, "초코"))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_OWNER_MISMATCH);
    }

    @Test
    void 지난_예약은_취소_시_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, "09:00", "10:00");
        Theme theme = new Theme("테마", "설명", "url").withId(1L);
        // fixedClock = 2026-05-12T10:00:00Z → 2026-05-12 09:00이 이미 지남
        Reservation reservation = new Reservation("브라운", LocalDate.of(2026, 5, 12), time, theme).withId(1L);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.cancel(1L, "브라운"))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PAST_RESERVATION_CANCEL_NOT_ALLOWED);
    }

    @Test
    void 소유자가_아니면_변경_시_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, "10:00", "11:00");
        Theme theme = new Theme("테마", "설명", "url").withId(1L);
        Reservation reservation = new Reservation("브라운", LocalDate.of(2099, 8, 5), time, theme).withId(1L);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        ReservationUpdateServiceDto dto = new ReservationUpdateServiceDto(1L, "초코", LocalDate.of(2099, 9, 1), 1L, 1L);

        assertThatThrownBy(() -> reservationService.update(dto))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVATION_OWNER_MISMATCH);
    }

    @Test
    void 과거_날짜로_변경_시_예외가_발생한다() {
        ReservationTime existingTime = new ReservationTime(1L, "10:00", "11:00");
        ReservationTime newTime = new ReservationTime(1L, "09:00", "10:00");
        Theme theme = new Theme("테마", "설명", "url").withId(1L);
        Reservation reservation = new Reservation("브라운", LocalDate.of(2099, 8, 5), existingTime, theme).withId(1L);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(timeService.findById(1L)).thenReturn(newTime);
        when(themeRepository.existsById(1L)).thenReturn(true);
        when(themeRepository.findById(1L)).thenReturn(theme);

        ReservationUpdateServiceDto dto = new ReservationUpdateServiceDto(1L, "브라운", LocalDate.of(2026, 5, 12), 1L, 1L);

        assertThatThrownBy(() -> reservationService.update(dto))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PAST_RESERVATION_NOT_ALLOWED);
    }

    @Test
    void 이미_예약된_시간으로_변경_시_예외가_발생한다() {
        ReservationTime existingTime = new ReservationTime(1L, "10:00", "11:00");
        ReservationTime newTime = new ReservationTime(2L, "14:00", "15:00");
        Theme theme = new Theme("테마", "설명", "url").withId(1L);
        Reservation reservation = new Reservation("브라운", LocalDate.of(2099, 8, 5), existingTime, theme).withId(1L);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(timeService.findById(2L)).thenReturn(newTime);
        when(themeRepository.existsById(1L)).thenReturn(true);
        when(themeRepository.findById(1L)).thenReturn(theme);
        when(holidayService.isHoliday(LocalDate.of(2099, 9, 1))).thenReturn(false);
        when(reservationRepository.isDuplicated(1L, newTime, LocalDate.of(2099, 9, 1))).thenReturn(true);

        ReservationUpdateServiceDto dto = new ReservationUpdateServiceDto(1L, "브라운", LocalDate.of(2099, 9, 1), 2L, 1L);

        assertThatThrownBy(() -> reservationService.update(dto))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_RESERVATION);
    }
}
