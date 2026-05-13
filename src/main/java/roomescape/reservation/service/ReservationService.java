package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
            throw new IllegalArgumentException("이미 예약된 스케줄입니다.");
        }

        if (schedule.isBefore()) {
            throw new IllegalArgumentException("이미 지나간 시간에는 예약할 수 없습니다.");
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if (!reservation.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("본인의 예약만 취소할 수 있습니다.");
        }

        if (reservation.getSchedule().isBefore()) {
            throw new IllegalArgumentException("이미 지나간 예약은 취소할 수 없습니다.");
        }

        reservationRepository.delete(reservationId);
    }

    // TODO: 너무 길다. (2026. 5. 13.)
    @Transactional
    public void update(Long reservationId, Long newScheduleId, String userName) {
        User currentUser = userService.findByName(userName);
        Schedule newSchedule = scheduleService.findById(newScheduleId);
        Reservation reservationToUpdate = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if (!reservationToUpdate.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("본인의 예약만 변경할 수 있습니다.");
        }
        if (reservationToUpdate.getSchedule().isBefore()) {
            throw new IllegalArgumentException("이미 지나간 예약은 변경할 수 없습니다.");
        }
        if (newSchedule.isBefore()) {
            throw new IllegalArgumentException("지나간 시간으로 예약을 변경할 수 없습니다.");
        }
        if (reservationRepository.existsByScheduleId(newScheduleId)) {
            throw new IllegalArgumentException("변경하려는 시간에 이미 예약이 있습니다.");
        }

        int updatedRows = reservationRepository.update(reservationId, newSchedule.getId(), currentUser.getId());

        if (updatedRows == 0) {
            throw new IllegalStateException("예약 변경에 실패했습니다. 예약 정보와 사용자 정보를 다시 확인해주세요.");
        }
    }
}
