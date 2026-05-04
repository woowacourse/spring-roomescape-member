package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRequest;
import roomescape.domain.reservation.ReservationResponse;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeResponse;
import roomescape.domain.theme.Theme;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.repository.ReservationQueryingDao;
import roomescape.repository.ReservationTimeQueryingDao;
import roomescape.repository.ReservationUpdatingDao;
import roomescape.repository.ThemeQueryingDao;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ReservationService {

    private final ReservationQueryingDao reservationQueryingDao;
    private final ReservationUpdatingDao reservationUpdatingDao;
    private final ReservationTimeQueryingDao reservationTimeQueryingDao;
    private final ThemeQueryingDao themeQueryingDao;

    public ReservationService(ReservationQueryingDao reservationQueryingDao, ReservationUpdatingDao reservationUpdatingDao, ReservationTimeQueryingDao reservationTimeQueryingDao, ThemeQueryingDao themeQueryingDao) {
        this.reservationQueryingDao = reservationQueryingDao;
        this.reservationUpdatingDao = reservationUpdatingDao;
        this.reservationTimeQueryingDao = reservationTimeQueryingDao;
        this.themeQueryingDao = themeQueryingDao;
    }

    public ReservationResponse read(Long id) {
        Reservation reservationById = reservationQueryingDao.findReservationById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        return ReservationResponse.from(reservationById);
    }

    public List<ReservationResponse> readAll() {
        List<Reservation> reservations = reservationQueryingDao.findAllReservations();
         return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse create(ReservationRequest reservationReq) {
        Long generatedId = reservationUpdatingDao.insert(reservationReq);

        ReservationTime reservationTimeById = reservationTimeQueryingDao.findReservationTimeById(reservationReq.getTimeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException(reservationReq.getTimeId()));
        Theme themeById = themeQueryingDao.findThemeById(reservationReq.getThemeId())
                .orElseThrow(() -> new ThemeNotFoundException(reservationReq.getThemeId()));
        return ReservationResponse.from(new Reservation(generatedId, reservationReq.getName(), reservationReq.getDate(), reservationTimeById, themeById));
    }

    @Transactional
    public void update(ReservationRequest newReservationReq, Long id) {
        reservationUpdatingDao.save(id, newReservationReq);
    }

    @Transactional
    public void delete(Long id) {
        int count = reservationUpdatingDao.delete(id);

        if (count == 0) {
            throw new ReservationNotFoundException(id);
        }
    }
}
