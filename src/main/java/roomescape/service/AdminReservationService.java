package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationResponse;
import roomescape.repository.ReservationDao;

@Service
public class AdminReservationService {

    private final ReservationDao reservationDao;

    public AdminReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationDao.findAll().stream()
                .map(r -> ReservationResponse.from(r, r.getTheme()))
                .toList();
    }
}
