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

import java.util.Collections;
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
        User user = userService.findOrCreateByName(request.name());
        Schedule schedule = scheduleService.findById(request.scheduleId());

        if (reservationRepository.existsByScheduleId(schedule.getId())) {
            throw new IllegalArgumentException("이미 예약된 스케줄입니다.");
        }

        Reservation reservation = new Reservation(user, schedule);
        return reservationRepository.create(reservation);
    }

    public ReservationsResponse findReservationsByUserName(String name) {
        return userService.findByName(name)
                .map(user -> {
                    List<Reservation> reservations = reservationRepository.findAllByUserId(user.getId());
                    return ReservationsResponse.from(reservations);
                })
                .orElse(ReservationsResponse.from(Collections.emptyList()));
    }


    public ReservationsResponse findAll() {
        List<Reservation> responses = reservationRepository.findAll();
        return ReservationsResponse.from(responses);
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.delete(id);
    }
}
