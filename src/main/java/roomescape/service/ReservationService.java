package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationCreateRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.ExistReservationException;
import roomescape.exception.IllegalReservationException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;
import roomescape.repository.TimeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(final TimeDao timeDao, ReservationDao reservationDao, ThemeDao themeDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findAllReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        return ReservationResponse.fromReservations(reservations);
    }

    public ReservationResponse saveReservation(ReservationCreateRequest request) {
        ReservationTime time = timeDao.findById(request.timeId());

        if (request.date().isEqual(LocalDate.now()) && time.isPast()) {
            throw new IllegalReservationException("[ERROR] 과거 시간은 예약할 수 없습니다.");
        }

        Theme theme = themeDao.findById(request.themeId());
        Reservation reservation = ReservationCreateRequest.toReservation(request, time, theme);

        if (reservationDao.existByDateAndTimeAndTheme(reservation.getDate(), reservation.getTimeId(), reservation.getThemeId())) {
            throw new ExistReservationException("[ERROR] 같은 날짜, 테마, 시간에 중복된 예약을 생성할 수 없습니다.");
        }

        Reservation newReservation = reservationDao.save(reservation);
        return ReservationResponse.fromReservation(newReservation);
    }

    public void deleteReservationById(Long id) {
        reservationDao.deleteById(id);
    }
}
