package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.request.ReservationRequest;
import roomescape.exception.NotFoundException;
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
        ReservationTime reservationTime = reservationTimeDAO.findReservationById(request.getTimeId());
        return reservationRepository.addReservation(new Reservation(request.getName(), request.getDate(), reservationTime));
    }

    public void deleteReservation(long id) {
        Long count = reservationRepository.countReservationById(id);
        if (count == null || count <= 0) {
            throw new NotFoundException("[ERROR] 존재하지 않는 예약입니다.");
        }
        reservationRepository.deleteReservation(id);
    }
}
