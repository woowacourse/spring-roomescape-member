package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationCreateRequest;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao dao;

    public ReservationService(ReservationDao dao) {
        this.dao = dao;
    }

    public List<Reservation> readReservations() {
        return dao.readReservations();
    }

    public Reservation createReservation(ReservationCreateRequest dto) {
        return dao.createReservation(dto.createReservation());
    }

    public void deleteReservation(long id) {
        dao.deleteReservation(id);
    }

}
