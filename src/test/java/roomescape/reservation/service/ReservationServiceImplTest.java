package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.holiday.service.HolidayService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.exception.TimeNotFoundException;
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
        reservationService = new ReservationServiceImpl(
                reservationRepository, timeService, themeRepository, holidayService);
    }

    @DisplayName("정상 입력인 경우, 저장된 예약을 반환한다.")
    @Test
    void create_정상_예약을_저장하고_반환() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
        Theme theme = new Theme("테마", "설명", "https://img.test/a.png").withId(1L);
        Reservation saved = new Reservation("라이", LocalDate.of(2026, 5, 20), time, 1L).withId(1L);

        when(timeService.findById(1L)).thenReturn(time);
        when(themeRepository.existsById(1L)).thenReturn(true);
        when(holidayService.isHoliday(any())).thenReturn(false);
        when(reservationRepository.isDuplicated(any(), any(), any())).thenReturn(false);
        when(reservationRepository.save(any())).thenReturn(saved);
        when(themeRepository.findById(1L)).thenReturn(theme);

        // when
        ReservationSaveServiceDto dto = new ReservationSaveServiceDto("라이", LocalDate.of(2026, 5, 20), 1L, 1L);
        Reservation result = reservationService.create(dto);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("라이");
    }

    @DisplayName("timeId가 null인 경우, IllegalArgumentException이 발생한다.")
    @Test
    void create_timeId가_null이면_예외() {
        // given
        ReservationSaveServiceDto dto = new ReservationSaveServiceDto("라이", LocalDate.of(2026, 5, 20), 1L, null);

        // when & then
        assertThatThrownBy(() -> reservationService.create(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간은 필수입니다.");
    }

    @DisplayName("존재하지 않는 timeId인 경우, TimeNotFoundException이 발생한다.")
    @Test
    void create_존재하지_않는_timeId이면_예외() {
        // given
        when(timeService.findById(999L)).thenThrow(new TimeNotFoundException(999L));
        ReservationSaveServiceDto dto = new ReservationSaveServiceDto("라이", LocalDate.of(2026, 5, 20), 1L, 999L);

        // when & then
        assertThatThrownBy(() -> reservationService.create(dto))
                .isInstanceOf(TimeNotFoundException.class);
    }

    @DisplayName("themeId가 null인 경우, IllegalArgumentException이 발생한다.")
    @Test
    void create_themeId가_null이면_예외() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
        when(timeService.findById(1L)).thenReturn(time);
        ReservationSaveServiceDto dto = new ReservationSaveServiceDto("라이", LocalDate.of(2026, 5, 20), null, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.create(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마는 필수입니다.");
    }

    @DisplayName("존재하지 않는 themeId인 경우, ThemeNotFoundException이 발생한다.")
    @Test
    void create_존재하지_않는_themeId이면_예외() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
        when(timeService.findById(1L)).thenReturn(time);
        when(themeRepository.existsById(999L)).thenReturn(false);
        ReservationSaveServiceDto dto = new ReservationSaveServiceDto("라이", LocalDate.of(2026, 5, 20), 999L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.create(dto))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @DisplayName("휴일에 예약을 시도하는 경우, 예외를 던진다.")
    @Test
    void create_휴일이면_예외() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
        LocalDate holiday = LocalDate.of(2026, 5, 20);
        when(timeService.findById(1L)).thenReturn(time);
        when(themeRepository.existsById(1L)).thenReturn(true);
        when(holidayService.isHoliday(holiday)).thenReturn(true);
        ReservationSaveServiceDto dto = new ReservationSaveServiceDto("라이", holiday, 1L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.create(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("휴일은 예약이 불가합니다.");
    }

    @DisplayName("같은 날짜/시간/테마에 중복 예약을 시도하는 경우, 예외를 던진다.")
    @Test
    void create_중복_예약이면_예외() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
        LocalDate date = LocalDate.of(2026, 5, 20);
        when(timeService.findById(1L)).thenReturn(time);
        when(themeRepository.existsById(1L)).thenReturn(true);
        when(holidayService.isHoliday(date)).thenReturn(false);
        when(reservationRepository.isDuplicated(any(), any(), any())).thenReturn(true);
        ReservationSaveServiceDto dto = new ReservationSaveServiceDto("라이", date, 1L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.create(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복 예약은 불가합니다.");
    }

    @DisplayName("전체 예약 목록을 반환한다.")
    @Test
    void getAll() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0), LocalTime.of(12, 0));
        List<Reservation> reservations = List.of(
                new Reservation("라이", LocalDate.of(2026, 5, 20), time, 1L).withId(1L),
                new Reservation("박", LocalDate.of(2026, 5, 21), time, 1L).withId(2L)
        );
        when(reservationRepository.findAll()).thenReturn(reservations);

        // when
        List<Reservation> result = reservationService.getAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(reservations);
    }

    @DisplayName("예약을 취소하는 경우, repository의 deleteById가 호출된다.")
    @Test
    void cancel() {
        // when
        reservationService.cancel(1L);

        // then
        verify(reservationRepository).deleteById(1L);
    }
}
