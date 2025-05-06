package roomescape;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import roomescape.dto.ReservationRequestDto;
import roomescape.model.*;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservedChecker;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private ReservedChecker reservedChecker;
    private ReservationTimeService reservationTimeService;
    private ThemeService themeService;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        reservedChecker = mock(ReservedChecker.class);
        reservationTimeService = mock(ReservationTimeService.class);
        themeService = mock(ThemeService.class);
        reservationService = new ReservationService(reservationRepository, reservedChecker,
                reservationTimeService, themeService);
    }


    @Test
    @DisplayName("예약을 추가하면 정상적으로 예약이 반환된다.")
    void addReservationTest() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(14, 0);
        ReservationTime reservationTime = new ReservationTime(1L, time);
        Theme theme = new Theme(2L, "공포", "무섭다","aaa");

        ReservationRequestDto requestDto = new ReservationRequestDto("벡터", date, 1L, 2L);

        when(reservationTimeService.getReservationTimeById(1L)).thenReturn(reservationTime);
        when(reservedChecker.contains(date, 1L, 2L)).thenReturn(false);
        when(themeService.getThemeById(2L)).thenReturn(theme);

        Reservation expectedReservation = requestDto.toEntity(null, reservationTime, theme);
        when(reservationRepository.addReservation(any())).thenReturn(expectedReservation);

        // when
        Reservation actual = reservationService.addReservation(requestDto);

        // then
        assertThat(actual).isEqualTo(expectedReservation);
    }

    @Test
    @DisplayName("과거 예약을 추가하면 예외가 발생한다.")
    void addPastReservationTest() {
        // given
        LocalDate date = LocalDate.now().minusDays(1);
        LocalTime time = LocalTime.of(14, 0);
        ReservationTime reservationTime = new ReservationTime(1L, time);
        ReservationRequestDto requestDto = new ReservationRequestDto("벡터", date, 1L, 2L);

        when(reservationTimeService.getReservationTimeById(1L)).thenReturn(reservationTime);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("과거 예약은 불가능합니다.");
    }

    @Test
    @DisplayName("중복 예약을 추가하면 예외가 발생한다.")
    void addReservationTDuplicateTest() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(14, 0);
        ReservationTime reservationTime = new ReservationTime(1L, time);
        ReservationRequestDto requestDto = new ReservationRequestDto("홍길동", date, 1L, 2L);

        when(reservationTimeService.getReservationTimeById(1L)).thenReturn(reservationTime);
        when(reservedChecker.contains(date, 1L, 2L)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Reservation already exists");
    }


    @Test
    @DisplayName("예약 삭제 실패하면 예외가 발생한다.")
    void deleteReservationTest() {
        when(reservationRepository.deleteReservation(999L)).thenReturn(0);

        assertThatThrownBy(() -> reservationService.deleteReservation(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("삭제할 예약이 존재하지 않습니다.");
    }
}
