package roomescape.reservation.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.infrastructure.JdbcReservationQueryDao;
import roomescape.reservation.infrastructure.dto.ReservationDetailData;

@Service
public class ReservationQueryService {

    public ReservationQueryService(JdbcReservationQueryDao reservationQueryDao) {
        this.reservationQueryDao = reservationQueryDao;
    }

    private final JdbcReservationQueryDao reservationQueryDao;

    public List<ReservationDetailData> getAllReservationDetails() {
        return reservationQueryDao.findAllReservationDetails();
    }

    public ReservationDetailData getReservationDetailById(Long id) {
        return reservationQueryDao.getReservationDetailById(id);
    }
}
