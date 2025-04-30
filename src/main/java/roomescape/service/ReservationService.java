package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.DuplicateReservationException;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse addReservation(ReservationRequest reservationRequest) {
        validateDuplicateReservation(reservationRequest);
        ReservationTime time = reservationTimeDao.findTimeById(reservationRequest.timeId());
        Theme theme = themeDao.findThemeById(reservationRequest.themeId());
        Reservation reservation = reservationDao.addReservation(
            ReservationRequest.toEntity(reservationRequest, time, theme));

        return ReservationResponse.from(reservation);
    }

    private void validateDuplicateReservation(ReservationRequest reservationRequest) {
        if (reservationDao.existReservationByDateTimeAndTheme(reservationRequest.date(), reservationRequest.timeId(), reservationRequest.themeId())) {
            throw new DuplicateReservationException();
        }
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationDao.findAllReservations().stream()
            .map(ReservationResponse::from)
            .toList();
    }

    public void removeReservation(Long id) {
        reservationDao.removeReservationById(id);
    }
}
