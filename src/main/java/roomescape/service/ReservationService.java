package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationRequest;
import roomescape.exception.AlreadyExistReservationException;
import roomescape.exception.IllegalThemeException;
import roomescape.exception.IllegalTimeException;
import roomescape.mapper.ReservationMapper;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;
import roomescape.repository.TimeDao;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper = new ReservationMapper();
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
        return reservations.stream()
                .map(reservationMapper::mapToResponse)
                .toList();
    }

    public ReservationResponse saveReservation(ReservationRequest request) {
        if (request.timeId() == null) {
            throw new IllegalTimeException("[ERROR] 유효하지 않은 형식의 예약 시간입니다.");
        }
        ReservationTime time = timeDao.findById(request.timeId());

        if (request.themeId() == null) {
            throw new IllegalThemeException("[ERROR] 유효하지 않은 형식의 테마입니다.");
        }
        Theme theme = themeDao.findById(request.themeId());
        Reservation reservation = reservationMapper.mapToReservation(request, time, theme);

        if (reservationDao.existByDateAndTimeAndTheme(reservation.getDate(), time.getStartAt(), reservation.getThemeId())) {
            throw new AlreadyExistReservationException("[ERROR] 같은 날짜, 테마, 시간에 중복된 예약을 생성할 수 없습니다.");
        }
        Reservation newReservation = reservationDao.save(reservation);

        return reservationMapper.mapToResponse(newReservation);
    }

    public void deleteReservationById(Long id) {
        reservationDao.deleteById(id);
    }
}
