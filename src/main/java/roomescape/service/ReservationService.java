package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDAO;
import roomescape.dao.ReservationTimeDAO;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationService(final ReservationDAO reservationDAO, final ReservationTimeDAO reservationTimeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public Reservation save(final ReservationRequest reservationRequest) {
        final ReservationTime reservationTime = reservationTimeDAO.findById(reservationRequest.timeId());
        final Reservation reservation = reservationRequest.toEntity(reservationTime);

        return reservationDAO.insert(reservation);
    }

    public List<Reservation> findAll() {
        return reservationDAO.selectAll();
    }

    public void delete(final long id) {
        reservationDAO.deleteById(id);
    }
}
