package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.ErrorCode;
import roomescape.exception.ForbiddenException;
import roomescape.reservation.dto.CreateReservationRequest;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.service.ScheduleService;
import roomescape.theme.model.Theme;
import roomescape.user.model.Role;
import roomescape.user.model.User;
import roomescape.user.service.UserService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservationServiceTest {

    private final User user = new User(1L, "user1", Role.USER);
    private final Theme theme = new Theme(1L, "공포", "설명", "경로", LocalTime.of(2, 0));
    private final Schedule schedule = new Schedule(1L, LocalDateTime.of(2024, 12, 10, 12, 0), theme);

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private UserService userService;
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        reservationRepository = Mockito.mock(ReservationRepository.class);
        userService = Mockito.mock(UserService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        Clock fixedClock = Clock.fixed(Instant.parse("2024-05-16T10:00:00Z"), ZoneId.of("Asia/Seoul"));

        reservationService = new ReservationService(userService, scheduleService, reservationRepository, fixedClock);
    }

    @Test
    void 새로운_예약을_성공적으로_생성한다() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(2L, "user1");
        Schedule availableSchedule = new Schedule(2L, LocalDateTime.of(2024, 12, 10, 15, 0), theme);

        when(userService.findByName("user1")).thenReturn(user);
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
    void 이미_예약된_스케줄에_예약을_시도하면_예외가_발생한다() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(1L, "user1");

        when(userService.findByName("user1")).thenReturn(user);
        when(scheduleService.findById(1L)).thenReturn(schedule);
        when(reservationRepository.existsByScheduleId(1L)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage(ErrorCode.ALREADY_RESERVED_SCHEDULE.getMessage());
    }

    @Test
    void 과거_시간의_스케줄로_예약을_시도하면_예외가_발생한다() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(3L, "user1");
        Schedule pastSchedule = new Schedule(3L, LocalDateTime.of(2024, 5, 16, 9, 0), theme);

        when(userService.findByName("user1")).thenReturn(user);
        when(scheduleService.findById(3L)).thenReturn(pastSchedule);

        // when & then
        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.RESERVATION_PAST_TIME.getMessage());
    }

    @Test
    void 본인의_예약이_아닌_것을_취소하려고_하면_예외가_발생한다() {
        // given
        User otherUser = new User(2L, "other", Role.USER);
        Reservation othersReservation = new Reservation(1L, otherUser, schedule);

        when(userService.findByName("user1")).thenReturn(user);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(othersReservation));

        // when & then
        assertThatThrownBy(() -> reservationService.delete(1L, "user1"))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ErrorCode.RESERVATION_NOT_OWNER.getMessage());
    }

    @Test
    void 이미_지나간_예약을_취소하려고_하면_예외가_발생한다() {
        // given
        Schedule pastSchedule = new Schedule(1L, LocalDateTime.of(2024, 5, 15, 12, 0), theme);
        Reservation passedReservation = new Reservation(1L, user, pastSchedule);

        when(userService.findByName("user1")).thenReturn(user);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(passedReservation));

        // when & then
        assertThatThrownBy(() -> reservationService.delete(1L, "user1"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.RESERVATION_ALREADY_PASSED.getMessage());
    }

    @Test
    void 예약_변경_시_업데이트된_행이_없으면_예외가_발생한다() {
        // given
        when(userService.findByName("user1")).thenReturn(user);
        when(scheduleService.findById(2L)).thenReturn(new Schedule(2L, LocalDateTime.of(2024, 12, 10, 15, 0), theme));

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(new Reservation(1L, user, schedule)));
        when(reservationRepository.update(1L, 2L, user.getId())).thenReturn(0);

        // when & then
        assertThatThrownBy(() -> reservationService.update(1L, 2L, "user1"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorCode.RESERVATION_UPDATE_FAILED.getMessage());
    }
}
