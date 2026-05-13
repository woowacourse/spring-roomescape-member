package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.CustomBusinessException;
import roomescape.exception.ErrorCode;
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

        if (reservationRepository.existsByScheduleId(schedule.getId())) {
            throw new CustomBusinessException(ErrorCode.ALREADY_RESERVED_SCHEDULE);
        }

        if (schedule.isBefore()) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PAST_TIME);
        }

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

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.getUser().getId().equals(currentUser.getId())) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_NOT_OWNER);
        }

        if (reservation.getSchedule().isBefore()) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_ALREADY_PASSED);
        }

        reservationRepository.delete(reservationId);
    }

    // TODO: 너무 길다. (2026. 5. 13.)
    @Transactional
    public void update(Long reservationId, Long newScheduleId, String userName) {
        User currentUser = userService.findByName(userName);
        Schedule newSchedule = scheduleService.findById(newScheduleId);
        Reservation reservationToUpdate = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomBusinessException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservationToUpdate.getUser().getId().equals(currentUser.getId())) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_NOT_OWNER);
        }
        if (reservationToUpdate.getSchedule().isBefore()) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_ALREADY_PASSED);
        }
        if (newSchedule.isBefore()) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_PAST_TIME);
        }
        if (reservationRepository.existsByScheduleId(newScheduleId)) {
            throw new CustomBusinessException(ErrorCode.ALREADY_RESERVED_SCHEDULE);
        }

        int updatedRows = reservationRepository.update(reservationId, newSchedule.getId(), currentUser.getId());

        if (updatedRows == 0) {
            throw new CustomBusinessException(ErrorCode.RESERVATION_UPDATE_FAILED);
        }
    }
}
