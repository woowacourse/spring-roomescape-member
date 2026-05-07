package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationTimeDAO;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.response.ReservationTimeCreateResponse;
import roomescape.reservation.dto.response.ReservationTimeFindAllResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeDAO reservationTimeDAO;

    public ReservationTimeService(ReservationTimeDAO reservationTimeDAO) {
        this.reservationTimeDAO = reservationTimeDAO;
    }

    public ReservationTimeCreateResponse create(ReservationTime reservationTime) {
        return reservationTimeDAO.insert(reservationTime);
    }

    public List<ReservationTimeFindAllResponse> findAll() {
        return reservationTimeDAO.findAll();
    }

    public void delete(Long id) {
        reservationTimeDAO.delete(id);
    }

    public ReservationTime findById(Long id) {
        return reservationTimeDAO.findById(id);
    }
}
