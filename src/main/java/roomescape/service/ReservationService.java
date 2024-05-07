package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationThemeDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.condition.ReservationInsertCondition;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationThemeDao reservationThemeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                              ReservationThemeDao reservationThemeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.reservationThemeDao = reservationThemeDao;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @Transactional
    public ReservationResponse insertReservation(ReservationRequest request) {
        validateDuplicate(request);
        ReservationTime time = reservationTimeDao.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        ReservationTheme theme = reservationThemeDao.findById(request.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        ReservationInsertCondition insertCondition = new ReservationInsertCondition(request.name(), request.date(),
                time, theme);
        Reservation inserted = reservationDao.insert(insertCondition);

        return new ReservationResponse(inserted);
    }

    private void validateDuplicate(ReservationRequest request) {
        if (reservationDao.hasSameReservation(request.date().toString(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 존재합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
