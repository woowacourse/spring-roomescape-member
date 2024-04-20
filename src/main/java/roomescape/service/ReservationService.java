package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.request.ReservationRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeDAO;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeDAO = reservationTimeRepository;
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.getAllReservations();
    }

    public Reservation addReservation(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeDAO.findReservationById(request.timeId());
        return reservationRepository.addReservation(new Reservation(request.name(), request.date(), reservationTime));
    }

    public void deleteReservation(long id) {
        reservationRepository.deleteReservation(id);
    }
}
