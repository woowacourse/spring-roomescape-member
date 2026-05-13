package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;
import roomescape.holiday.service.HolidayService;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
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
}
