package roomescape.reservation.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.application.dao.ReservationDetailDao;
import roomescape.reservation.application.dto.ReservationDetail;
import roomescape.reservation.application.dto.ReservationResult;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReservationQueryService {

    private final ReservationDetailDao reservationDetailDao;

    public List<ReservationResult> findAll() {
        List<ReservationDetail> result = reservationDetailDao.findAll();
        return result.stream()
                .map(ReservationResult::from)
                .toList();
    }

    public List<ReservationResult> findByName(String username) {
        List<ReservationDetail> result = reservationDetailDao.findByName(username);
        return result.stream()
                .map(ReservationResult::from)
                .toList();
    }
}
