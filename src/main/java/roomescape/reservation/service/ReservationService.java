package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.ErrorCode;
import roomescape.exception.ForbiddenException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.dto.CreateReservationRequest;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.service.ScheduleService;
import roomescape.user.model.User;
import roomescape.user.service.UserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final UserService userService;
    private final ScheduleService scheduleService;
    private final ReservationRepository reservationRepository;
    private final Clock clock;

    public ReservationService(UserService userService, ScheduleService scheduleService, ReservationRepository reservationRepository, Clock clock) {
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.reservationRepository = reservationRepository;
        this.clock = clock;
    }

    @Transactional
    public Long create(CreateReservationRequest request) {
        User user = userService.findByName(request.name());
        Schedule schedule = scheduleService.findById(request.scheduleId());
        ensureScheduleIsBookable(schedule);

        Reservation reservation = new Reservation(user, schedule);
        return reservationRepository.create(reservation);
    }

    public ReservationsResponse findReservationsByUserName(String name) {
        User user = userService.findByName(name);
        List<Reservation> reservations = reservationRepository.findAllByUserId(user.getId());

        return ReservationsResponse.from(reservations);
    }

    public ReservationsResponse findAll() {
        List<Reservation> responses = reservationRepository.findAll();
        return ReservationsResponse.from(responses);
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.delete(id);
    }

    @Transactional
    public void delete(Long reservationId, String userName) {
        User currentUser = userService.findByName(userName);
        ensureReservationCanBeModified(reservationId, currentUser);
        reservationRepository.delete(reservationId);
    }

    @Transactional
    public void update(Long reservationId, Long newScheduleId, String userName) {
        User currentUser = userService.findByName(userName);
        Schedule newSchedule = scheduleService.findById(newScheduleId);

        ensureReservationCanBeModified(reservationId, currentUser);
        ensureScheduleIsBookable(newSchedule);

        int updatedRows = reservationRepository.update(reservationId, newSchedule.getId(), currentUser.getId());
        if (updatedRows == 0) {
            throw new BadRequestException(ErrorCode.RESERVATION_UPDATE_FAILED);
        }
    }

    public boolean existsByScheduleId(Long scheduleId) {
        return reservationRepository.existsByScheduleId(scheduleId);
    }

    private void ensureScheduleIsBookable(Schedule Schedule) {
        if (Schedule.isBefore(LocalDateTime.now(clock))) {
            throw new BadRequestException(ErrorCode.RESERVATION_PAST_TIME);
        }
        if (this.existsByScheduleId(Schedule.getId())) {
            throw new ConflictException(ErrorCode.ALREADY_RESERVED_SCHEDULE);
        }
    }

    private void ensureReservationCanBeModified(Long reservationId, User currentUser) {
        Reservation reservationToUpdate = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservationToUpdate.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(ErrorCode.RESERVATION_NOT_OWNER);
        }
        if (reservationToUpdate.getSchedule().isBefore(LocalDateTime.now(clock))) {
            throw new BadRequestException(ErrorCode.RESERVATION_ALREADY_PASSED);
        }
    }
}
