package roomescape.reservationtime.service;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.domain.Reservation;
import roomescape.global.exception.InvalidRequestException;
import roomescape.global.exception.NotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("이미 존재하는 예약 시간을 생성하면 예외가 발생한다.")
    public void create_fail() {
        // given
        LocalTime startAt = LocalTime.of(23, 59);
        given(reservationTimeRepository.existsByStartAt(startAt)).willReturn(true);

        // when, then
        assertThatThrownBy(() -> reservationTimeService.create(startAt))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("이미 존재하는 예약 시간입니다.");

        then(reservationTimeRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("특정 날짜 및 테마의 예약 가능한 시간들을 반환한다.")
    public void findAvailableTimes_success() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime time2 = new ReservationTime(2L, LocalTime.of(12, 0));
        Theme targetTheme = new Theme(1L, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");
        LocalDate targetDate = LocalDate.of(2023, 8, 5);
        Reservation targetReservation = new Reservation(1L, "브라운", targetDate, time, targetTheme);

        given(themeRepository.existsById(targetTheme.getId())).willReturn(true);
        given(reservationRepository.findByDateAndThemeId(targetDate, targetTheme.getId()))
                .willReturn(List.of(targetReservation));
        given(reservationTimeRepository.findAll()).willReturn(List.of(time, time2));

        // when
        List<ReservationTimeAvailability> availableTimes = reservationTimeService.findAvailableTimes(targetDate, targetTheme.getId());

        // then
        assertThat(availableTimes).hasSize(2)
                .extracting(ReservationTimeAvailability::getReservationTime,
                        ReservationTimeAvailability::isAvailable)
                .containsExactlyInAnyOrder(Tuple.tuple(time, false), Tuple.tuple(time2, true));
    }

    @Test
    @DisplayName("특정 날짜 및 테마의 예약 가능한 시간들을 찾을 때 테마 id가 없으면 예외가 발생한다.")
    public void findAvailableTimes_fail() {
        // given
        LocalDate date = LocalDate.of(2026, 5, 6);
        Long themeId = 37L;

        given(themeRepository.existsById(themeId)).willReturn(false);

        // when, then
        assertThatThrownBy(() -> reservationTimeService.findAvailableTimes(date, themeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 테마입니다.");

        then(reservationRepository).should(never()).findByDateAndThemeId(any(), any());
        then(reservationTimeRepository).should(never()).findAll();
    }
}
