package roomescape.reservation.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.presentation.controller.ReservationSearchCondition;
import roomescape.reservation.infrastructure.JdbcReservationQueryDao;
import roomescape.reservation.infrastructure.dto.ReservationDetailData;

@Service
public class ReservationQueryService {

    private final JdbcReservationQueryDao reservationQueryDao;

    public ReservationQueryService(JdbcReservationQueryDao reservationQueryDao) {
        this.reservationQueryDao = reservationQueryDao;
    }

    public List<ReservationDetailData> getReservationDetails(ReservationSearchCondition condition) {
        if(condition.isEmpty()) {
            return reservationQueryDao.findAllReservationDetails();
        }
        return reservationQueryDao.findByCondition(condition);
    }

    public ReservationDetailData getReservationDetailById(Long id) {
        return reservationQueryDao.getReservationDetailById(id);
    }
}
