package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.dto.ReservationRequest;
import roomescape.domain.TimeSlot;
import roomescape.domain.dto.ReservationResponse;
import roomescape.repository.ReservationDAO;
import roomescape.repository.TimeDAO;

import java.util.List;

@Service
public class ReservationService {
    private final TimeDAO timeDAO;
    private final ReservationDAO reservationDAO;

    public ReservationService(TimeDAO timeDAO, ReservationDAO reservationDAO) {
        this.timeDAO = timeDAO;
        this.reservationDAO = reservationDAO;
    }

    public List<ReservationResponse> findEntireReservationList() {
        return reservationDAO.read()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse create(ReservationRequest reservationRequest) {
        Long reservationId = reservationDAO.create(reservationRequest);
        TimeSlot timeSlot = timeDAO.findById(reservationRequest.timeId());

        Reservation reservation = reservationRequest.toEntity(reservationId, timeSlot);
        return ReservationResponse.from(reservation);
    }

    public void delete(Long id) {
        reservationDAO.delete(id);
    }
}
