package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationCreateRequest;

import java.time.LocalDateTime;
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
        Reservation reservation = dto.createReservation();
        validateReservation(reservation);
        return dao.createReservation(reservation);
    }

    private void validateReservation(Reservation reservation) {
        if (reservation.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("예약은 현재 시간 이후여야 합니다.");
        }
    }

    public void deleteReservation(long id) {
        dao.deleteReservation(id);
    }

}
