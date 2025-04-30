package roomescape.service;

import java.util.List;
import java.util.Optional;
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

    public ReservationService(
        ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao,
        ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
            .stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public ReservationResponse create(ReservationRequest request) {
        int count = reservationDao.getCountByDateAndTimeId(request.date(), request.timeId());
        if (count != 0) {
            throw new IllegalArgumentException("[ERROR] 해당 날짜와 시간에 대한 예약이 이미 존재합니다.");
        }

        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(request.timeId());
        Optional<Theme> theme = themeDao.findById(request.themeId());
        if (reservationTime.isEmpty()) {
            throw new IllegalArgumentException("해당하는 시간이 없습니다");
        }
        if (theme.isEmpty()) {
            throw new IllegalArgumentException("해당하는 테마가 없습니다");
        }
        Reservation reservation = new Reservation(
            request.name(),
            request.date(),
            reservationTime.get(),
            theme.get()
        );
        return ReservationResponse.from(reservationDao.save(reservation));
    }

    public boolean deleteReservation(Long id) {
        int deleteCount = reservationDao.deleteById(id);
        return deleteCount != 0;
    }
}
