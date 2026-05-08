package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse addReservation(ReservationRequest request) {
        ReservationTime reservationTime = getTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        Reservation reservation = request.toReservation(reservationTime, theme);
        Reservation savedReservation = reservationDao.insert(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private ReservationTime getTime(long timeId) {
        return reservationTimeDao.selectById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
    }

    private Theme getTheme(long themeId) {
        return themeDao.selectById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationDao.select();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void delete(Long reservationId) {
        int deleted = reservationDao.delete(reservationId);
        if (deleted == 0) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
    }
}
