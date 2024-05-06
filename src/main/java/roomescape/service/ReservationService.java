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
import roomescape.repository.JdbcReservationDao;
import roomescape.repository.ThemeDao;
import roomescape.repository.JdbcTimeDao;

@Service
public class ReservationService {

    private final JdbcReservationDao jdbcReservationDao;
    private final JdbcTimeDao jdbcTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(final JdbcTimeDao jdbcTimeDao, JdbcReservationDao jdbcReservationDao, ThemeDao themeDao) {
        this.jdbcTimeDao = jdbcTimeDao;
        this.jdbcReservationDao = jdbcReservationDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = jdbcReservationDao.findAll();
        return ReservationResponse.fromReservations(reservations);
    }

    public long save(ReservationCreateRequest request) {
        if (request.date().isBefore(LocalDate.now())) {
            throw new IllegalReservationException("[ERROR] 과거 날짜는 예약할 수 없습니다.");
        }

        ReservationTime time = jdbcTimeDao.findById(request.timeId());
        if (request.date().isEqual(LocalDate.now()) && time.isPast()) {
            throw new IllegalReservationException("[ERROR] 과거 시간은 예약할 수 없습니다.");
        }

        Theme theme = themeDao.findById(request.themeId());
        Reservation reservation = ReservationCreateRequest.toReservation(request, time, theme);

        if (jdbcReservationDao.existByDateAndTimeAndTheme(reservation.getDate(), reservation.getTimeId(), reservation.getThemeId())) {
            throw new ExistReservationException("[ERROR] 같은 날짜, 테마, 시간에 중복된 예약을 생성할 수 없습니다.");
        } // TODO: IllegalReservationException와 통합?

        return jdbcReservationDao.save(reservation);
    }

    public void deleteById(Long id) {
        jdbcReservationDao.deleteById(id);
    }
}
