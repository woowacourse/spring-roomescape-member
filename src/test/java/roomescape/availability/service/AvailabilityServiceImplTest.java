package roomescape.availability.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.holiday.repository.HolidayRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.TimeRepository;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceImplTest {

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private TimeRepository timeRepository;

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private AvailabilityServiceImpl availabilityService;

    @BeforeEach
    void setUp() {
        availabilityService = new AvailabilityServiceImpl(
                themeRepository,
                timeRepository,
                holidayRepository,
                reservationRepository
        );
    }

    @Test
    void getAvailableTimes_테마가없으면_예외() {
        LocalDate date = LocalDate.of(2026, 5, 6);
        when(themeRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> availabilityService.getAvailableTimes(999L, date))
                .isInstanceOf(ThemeNotFoundException.class)
                .hasMessage("테마가 존재하지 않습니다. id=999");

        verifyNoInteractions(timeRepository, holidayRepository, reservationRepository);
    }

    @Test
    void getAvailableTimes_휴일이면_빈리스트() {
        LocalDate date = LocalDate.of(2026, 5, 6);
        when(themeRepository.existsById(1L)).thenReturn(true);
        when(holidayRepository.existsByDate(date)).thenReturn(true);

        assertThat(availabilityService.getAvailableTimes(1L, date)).isEmpty();

        verify(holidayRepository).existsByDate(date);
        verifyNoInteractions(timeRepository, reservationRepository);
    }

    @Test
    void getAvailableTimes_예약된시간은_제외한다() {
        LocalDate date = LocalDate.of(2026, 5, 6);
        when(themeRepository.existsById(1L)).thenReturn(true);
        when(holidayRepository.existsByDate(date)).thenReturn(false);
        when(reservationRepository.findTimeIdsByThemeIdAndDate(1L, date)).thenReturn(List.of(2L));

        List<ReservationTime> allTimes = List.of(
                new ReservationTime(1L, "10:00", "12:00"),
                new ReservationTime(2L, "12:00", "14:00"),
                new ReservationTime(3L, "14:00", "16:00")
        );
        when(timeRepository.findAll()).thenReturn(allTimes);

        List<ReservationTime> results = availabilityService.getAvailableTimes(1L, date);

        assertThat(results).extracting(ReservationTime::getId)
                .containsExactly(1L, 3L);
    }

    @Test
    void getAvailableTimes_예약이없으면_전체시간을_반환한다() {
        LocalDate date = LocalDate.of(2026, 5, 6);
        when(themeRepository.existsById(1L)).thenReturn(true);
        when(holidayRepository.existsByDate(date)).thenReturn(false);
        when(reservationRepository.findTimeIdsByThemeIdAndDate(1L, date)).thenReturn(Collections.emptyList());

        List<ReservationTime> allTimes = List.of(
                new ReservationTime(1L, "10:00", "12:00"),
                new ReservationTime(2L, "12:00", "14:00")
        );
        when(timeRepository.findAll()).thenReturn(allTimes);

        assertThat(availabilityService.getAvailableTimes(1L, date))
                .isEqualTo(allTimes);
    }
}
