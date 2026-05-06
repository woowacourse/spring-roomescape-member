package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.dto.ReservationIdResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.user.model.User;
import roomescape.user.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    public ReservationIdResponse create(ReservationRequest request) {
        Schedule schedule = scheduleRepository.findById(request.scheduleId());
        User user = userRepository.findById(request.userId());

        Reservation reservation = new Reservation(user, schedule, schedule.getTheme());
        Long reservationId = reservationRepository.create(reservation);
        Reservation savedReservation = new Reservation(reservationId, user, schedule, schedule.getTheme());
        return ReservationIdResponse.from(savedReservation);
    }

    public ReservationsResponse findAll() {
        List<Reservation> responses = reservationRepository.findAll();
        return ReservationsResponse.from(responses);
    }

    @Transactional
    public void delete(long id) {
        reservationRepository.delete(id);
    }
}
