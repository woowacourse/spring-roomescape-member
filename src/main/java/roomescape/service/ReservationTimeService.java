package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeDAO;

@Service
public class ReservationTimeService {

    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationTimeService(ReservationTimeDAO reservationTimeDAO) {
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public List<ReservationTime> getReservationTimes() {
        return reservationTimeDAO.findAll();
    }

    public ReservationTime createReservationTime(LocalTime startAt) {
        ReservationTime reservationTime = new ReservationTime(null, startAt);
        Long newReservationTimeId = reservationTimeDAO.save(reservationTime);
        return reservationTimeDAO.findById(newReservationTimeId);
    }

    public void deleteReservationTime(Long id) {
        reservationTimeDAO.deleteById(id);
    }
}