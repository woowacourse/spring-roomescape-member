package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationCreateRequest;
import roomescape.dto.ReservationResponse;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> readReservations() {
        return reservationDao.readReservations()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse createReservation(ReservationCreateRequest dto) {
        ReservationTime time = readTimeByTimeId(dto.timeId());
        Theme theme = readThemeByThemeId(dto.themeId());
        Reservation reservation = dto.createReservation(time, theme);

        validateIsAvailable(reservation);
        Reservation createdReservation = reservationDao.createReservation(reservation);
        return ReservationResponse.from(createdReservation);
    }

    private ReservationTime readTimeByTimeId(Long timeId) {
        return timeDao.readTimeById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 시간이 존재하지 않습니다."));
    }

    private Theme readThemeByThemeId(Long themeId) {
        return themeDao.readThemeById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));
    }

    private void validateIsAvailable(Reservation reservation) {
        if (reservation.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("예약은 현재 시간 이후여야 합니다.");
        }
        if (isAlreadyReserved(reservation)) {
            throw new IllegalArgumentException("해당 시간대 해당 테마 예약은 이미 존재합니다.");
        }
    }

    private boolean isAlreadyReserved(Reservation reservation) {
        return reservationDao.isExistReservationByDateAndTimeIdAndThemeId(
                reservation.getDate(), reservation.getTimeId(), reservation.getThemeId());
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteReservation(id);
    }
}
