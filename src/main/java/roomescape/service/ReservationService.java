package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationDAO;
import roomescape.repository.ReservationTimeDAO;

@Service
public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationService(ReservationDAO reservationDAO, ReservationTimeDAO reservationTimeDAO) {
        this.reservationDAO = reservationDAO;
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public List<Reservation> getReservations() {
        return reservationDAO.findAll();
    }

    public Reservation createReservation(String name, LocalDate date, Long timeId) {
        Reservation reservation = new Reservation(
                null,
                name,
                date,
                reservationTimeDAO.findById(timeId) // TODO: 예외 처리 필요 (시간이 존재하지 않는 경우)
        );
        Long newReservationId = reservationDAO.save(reservation);
        return reservationDAO.findById(newReservationId);
    }

    public void deleteReservation(Long id) {
        reservationDAO.deleteById(id);
    }
}
