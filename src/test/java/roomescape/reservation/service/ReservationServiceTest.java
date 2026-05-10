package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.service.ScheduleService;
import roomescape.theme.model.Theme;
import roomescape.user.model.Role;
import roomescape.user.model.User;
import roomescape.user.service.UserService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservationServiceTest {

    private final User user = new User(1L, "user1", Role.USER);
    private final Theme theme = new Theme(1L, "공포", "설명", "경로", LocalTime.of(2, 0));
    private final Schedule schedule = new Schedule(1L, LocalDateTime.of(2026, 12, 10, 12, 0), theme);
    private final Reservation reservation = new Reservation(1L, user, schedule);

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private UserService userService;
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        reservationRepository = Mockito.mock(ReservationRepository.class);
        userService = Mockito.mock(UserService.class);
        scheduleService = Mockito.mock(ScheduleService.class);

        reservationService = new ReservationService(userService, scheduleService, reservationRepository);
    }

    @Test
    @DisplayName("새로운 예약을 성공적으로 생성한다.")
    void createReservationSuccessfully() {
        // given
        ReservationRequest request = new ReservationRequest(2L, "user1");
        Schedule availableSchedule = new Schedule(2L, LocalDateTime.of(2026, 12, 10, 15, 0), theme);

        when(userService.findOrCreateByName("user1")).thenReturn(user);
        when(scheduleService.findById(2L)).thenReturn(availableSchedule);
        when(reservationRepository.existsByScheduleId(2L)).thenReturn(false);
        when(reservationRepository.create(any(Reservation.class))).thenReturn(100L);

        // when
        Long createdId = reservationService.create(request);

        // then
        assertThat(createdId).isEqualTo(100L);
        verify(reservationRepository).create(any(Reservation.class)); // create가 호출되었는지 검증
    }

    @Test
    @DisplayName("이미 예약된 스케줄에 예약을 시도하면 예외가 발생한다.")
    void createReservationOnReservedSchedule() {
        // given
        ReservationRequest request = new ReservationRequest(1L, "user1");

        when(userService.findOrCreateByName("user1")).thenReturn(user);
        when(scheduleService.findById(1L)).thenReturn(schedule);
        when(reservationRepository.existsByScheduleId(1L)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 스케줄입니다.");
    }
}
