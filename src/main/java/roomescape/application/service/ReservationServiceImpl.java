package roomescape.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.ReservationRequest;
import roomescape.application.dto.ReservationResponse;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final String ERROR_DUPLICATE_RESERVATION = "중복된 예약은 생성이 불가능합니다.";

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationServiceImpl(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                                  ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationResponse createReservation(ReservationRequest request) {
        Reservation reservationWithoutId = toReservation(request);
        validateForCreation(reservationWithoutId);

        Reservation savedReservation = saveReservation(reservationWithoutId);
        return new ReservationResponse(savedReservation);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }

    private Reservation toReservation(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        Theme theme = themeDao.findById(request.themeId());
        return request.toReservationWith(reservationTime, theme);
    }

    private void validateForCreation(Reservation reservationWithoutId) {
        reservationWithoutId.validatePastDateTime();
        if (reservationDao.existByDateAndTimeId(reservationWithoutId.getDate(),
                reservationWithoutId.getTheme().getId())) {
            throw new IllegalArgumentException(ERROR_DUPLICATE_RESERVATION);
        }
    }

    private Reservation saveReservation(Reservation reservationWithoutId) {
        Long reservationId = reservationDao.save(reservationWithoutId);
        return reservationWithoutId.copyWithId(reservationId);
    }
}
