package roomescape.theme.service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.holiday.repository.HolidayRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.dto.ThemeBestServiceDto;
import roomescape.theme.service.dto.ThemeSaveServiceDto;
import roomescape.time.service.TimeService;

@ExtendWith(MockitoExtension.class)
class ThemeServiceImplTest {

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private TimeService timeService;

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private ThemeServiceImpl themeService;
    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2026-05-06T00:00:00Z"), ZoneId.of("Asia/Seoul"));
        themeService = new ThemeServiceImpl(
                themeRepository,
                timeService,
                holidayRepository,
                reservationRepository,
                clock,
                7,
                10
        );
    }

    @Test
    void getAll() {
        List<Theme> themes = List.of(
                new Theme("a", "b", "c").withId(1L),
                new Theme("d", "e", "f").withId(2L));
        when(themeRepository.findAll()).thenReturn(themes);

        assertThat(themeService.getAll()).isEqualTo(themes);
        verify(themeRepository).findAll();
    }

    @Test
    void create() {
        ThemeSaveServiceDto dto = new ThemeSaveServiceDto("이름", "설명", "https://url");
        Theme persisted = new Theme("이름", "설명", "https://url").withId(10L);
        when(themeRepository.save(any(Theme.class))).thenReturn(persisted);

        Theme result = themeService.create(dto);

        assertThat(result).isEqualTo(persisted);

        ArgumentCaptor<Theme> captor = ArgumentCaptor.forClass(Theme.class);
        verify(themeRepository).save(captor.capture());
        Theme passed = captor.getValue();
        assertThat(passed.getId()).isNull();
        assertThat(passed.getName()).isEqualTo("이름");
        assertThat(passed.getDescription()).isEqualTo("설명");
        assertThat(passed.getImageUrl()).isEqualTo("https://url");
    }

    @Test
    void deleteById() {
        when(themeRepository.deleteById(1L)).thenReturn(true);

        themeService.deleteById(1L);

        verify(themeRepository).deleteById(1L);
    }

    @Test
    void deleteById_없으면_예외() {
        when(themeRepository.deleteById(99L)).thenReturn(false);

        assertThatThrownBy(() -> themeService.deleteById(99L))
                .isInstanceOf(ThemeNotFoundException.class)
                .hasMessage("테마가 존재하지 않습니다. id=99")
                .extracting(e -> ((ThemeNotFoundException) e).getId())
                .isEqualTo(99L);
    }

    @Test
    void getAvailableTimes_테마가없으면_예외() {
        LocalDate date = LocalDate.of(2026, 5, 6);
        when(themeRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> themeService.getAvailableTimes(999L, date))
                .isInstanceOf(ThemeNotFoundException.class)
                .hasMessage("테마가 존재하지 않습니다. id=999");

        verifyNoInteractions(timeService, holidayRepository, reservationRepository);
    }

    @Test
    void getAvailableTimes_휴일이면_빈리스트() {
        LocalDate date = LocalDate.of(2026, 5, 6);
        when(themeRepository.existsById(1L)).thenReturn(true);
        when(holidayRepository.existsByDate(date)).thenReturn(true);

        assertThat(themeService.getAvailableTimes(1L, date)).isEmpty();

        verify(holidayRepository).existsByDate(date);
        verifyNoInteractions(timeService, reservationRepository);
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
        when(timeService.findAll()).thenReturn(allTimes);

        List<ReservationTime> results = themeService.getAvailableTimes(1L, date);

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
        when(timeService.findAll()).thenReturn(allTimes);

        assertThat(themeService.getAvailableTimes(1L, date))
                .isEqualTo(allTimes);
    }

    @Test
    void getBestThemes_주입받은_Clock의_날짜를_기준으로_조회한다() {
        List<Theme> themes = List.of(new Theme("이름", "설명", "https://url").withId(1L));
        when(themeRepository.findBestThemesByDate(any(ThemeBestServiceDto.class))).thenReturn(themes);

        List<Theme> result = themeService.getBestThemes();

        assertThat(result).isEqualTo(themes);

        ArgumentCaptor<ThemeBestServiceDto> captor = ArgumentCaptor.forClass(ThemeBestServiceDto.class);
        verify(themeRepository).findBestThemesByDate(captor.capture());

        ThemeBestServiceDto dto = captor.getValue();
        assertThat(dto.date()).isEqualTo(LocalDate.of(2026, 5, 6));
        assertThat(dto.dayCount()).isEqualTo(7);
        assertThat(dto.rankCount()).isEqualTo(10);
    }
}
