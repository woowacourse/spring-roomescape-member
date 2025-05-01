package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.ExistedReservationException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.reservation.Reservation;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dao.ReservationTimeDao;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.theme.Theme;
import roomescape.theme.dao.ThemeDao;

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

    public List<ReservationResponse> findAll() {
        List<Reservation> reservationDaoAll = reservationDao.findAll();
        return reservationDaoAll.stream()
                .map(ReservationResponse::toDto)
                .toList();
    }

    public ReservationResponse create(ReservationCreateRequest request) {
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId())
                .orElseThrow(ReservationTimeNotFoundException::new);
        Theme theme = themeDao.findById(request.themeId()).orElseThrow(ThemeNotFoundException::new);
        Reservation reservation = Reservation.createWithoutId(
                request.name(),
                request.date(),
                reservationTime,
                theme
        );
        validateDuplicate(request.date(), reservationTime.getStartAt());
        Reservation savedReservation = reservationDao.create(reservation);
        return new ReservationResponse(
                savedReservation.getId(),
                savedReservation.getName(),
                savedReservation.getDate(),
                new ReservationTimeResponse(
                        savedReservation.getId(), savedReservation.getReservationTime().getStartAt()
                ),
                savedReservation.getTheme().getName()
        );
    }

    private void validateDuplicate(LocalDate date, LocalTime startAt) {
        if (reservationDao.findByDateTime(date, startAt).isPresent()) {
            throw new ExistedReservationException();
        }
    }

    public void delete(Long id) {
        reservationDao.findById(id).orElseThrow(ReservationNotFoundException::new);
        reservationDao.delete(id);
    }
}
