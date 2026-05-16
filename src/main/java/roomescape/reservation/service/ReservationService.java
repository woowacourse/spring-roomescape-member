package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.ErrorCode;
import roomescape.exception.ForbiddenException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.service.ScheduleService;
import roomescape.user.model.User;
import roomescape.user.service.UserService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final UserService userService;
    private final ScheduleService scheduleService;
    private final ReservationRepository reservationRepository;

    public ReservationService(UserService userService, ScheduleService scheduleService, ReservationRepository reservationRepository) { // 생성자 주입
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Long create(ReservationRequest request) {
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

    private void ensureScheduleIsBookable(Schedule Schedule) {
        if (Schedule.isBefore()) {
            throw new BadRequestException(ErrorCode.RESERVATION_PAST_TIME);
        }
        if (reservationRepository.existsByScheduleId(Schedule.getId())) {
            throw new ConflictException(ErrorCode.ALREADY_RESERVED_SCHEDULE);
        }
    }

    private void ensureReservationCanBeModified(Long reservationId, User currentUser) {
        Reservation reservationToUpdate = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservationToUpdate.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(ErrorCode.RESERVATION_NOT_OWNER);
        }
        if (reservationToUpdate.getSchedule().isBefore()) {
            throw new BadRequestException(ErrorCode.RESERVATION_ALREADY_PASSED);
        }
    }
}
