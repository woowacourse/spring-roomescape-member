package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.reservation.AvailableReservationResponse;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.exception.InvalidValueException;

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
        List<Reservation> reservations = reservationDao.readAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<AvailableReservationResponse> findTimeByDateAndThemeID(String date, Long themeId) {
        LocalDate today = LocalDate.now();
        ReservationDate reservationDate = ReservationDate.from(date);
        List<ReservationTime> reservationTimes = reservationTimeDao.readAll();
        List<Long> ids = reservationDao.readTimeIdsByDateAndThemeId(reservationDate, themeId);
        List<ReservationTime> filteredTimes = filterByDate(reservationDate, reservationTimes, today);
        return filteredTimes.stream()
                .map(time ->
                        AvailableReservationResponse.of(
                                time.getStartAt().toStringTime(),
                                time.getId(),
                                ids.contains(time.getId())
                        ))
                .toList();
    }

    public ReservationResponse add(ReservationCreateRequest request) {
        LocalDate today = LocalDate.now();
        validateNotExistReservationTime(request.getTimeId());
        ReservationTime reservationTime = reservationTimeDao.readById(request.getTimeId());
        Theme theme = themeDao.readById(request.getThemeId());
        Reservation reservation = request.toDomain(reservationTime, theme);
        validateDate(reservation.getDate(), today);
        validateDuplicate(reservation.getDate(), reservation.getReservationTime(), reservation.getTheme());
        Reservation result = reservationDao.create(reservation);
        validatePastTimeWhenToday(reservation, reservationTime, today);
        return ReservationResponse.from(result);
    }

    public void delete(Long id) {
        validateNull(id);
        validateNotExistReservation(id);
        reservationDao.delete(id);
    }

    private void validateDuplicate(
            ReservationDate reservationDate,
            ReservationTime reservationTime,
            Theme theme) {
        if (reservationDao.exist(reservationDate, reservationTime, theme)) {
            throw new InvalidValueException("중복된 예약을 생성할 수 없습니다.");
        }
    }

    private List<ReservationTime> filterByDate(ReservationDate reservationDate,
                                               List<ReservationTime> reservationTimes,
                                               LocalDate date) {
        if (reservationDate.isSameDate(date)) {
            return reservationTimes.stream()
                    .filter(time -> !time.isBeforeTime(LocalTime.now()))
                    .toList();
        }
        return reservationTimes;
    }

    private void validateDate(ReservationDate reservationDate, LocalDate date) {
        if (reservationDate.isBeforeDate(date)) {
    private void validateDate(Reservation reservation) {
        if (reservation.isBeforeDate(LocalDate.now())) {
            throw new InvalidValueException("예약일은 오늘보다 과거일 수 없습니다.");
        }
    }

    private void validatePastTimeWhenToday(Reservation reservation, ReservationTime reservationTime, LocalDate date) {
        if (reservation.isSameDate(date) && reservationTime.isBeforeNow()) {
    private void validatePastTimeWhenToday(Reservation reservation) {
        if (reservation.isSameDate(LocalDate.now()) && reservation.isBeforeTime(LocalTime.now())) {
            throw new InvalidValueException("현재보다 이전 시간을 예약할 수 없습니다.");
        }
    }

    private void validateNull(Long id) {
        if (id == null) {
            throw new InvalidValueException("예약 아이디는 비어있을 수 없습니다.");
        }
    }

    private void validateNotExistReservation(Long id) {
        if (!reservationDao.exist(id)) {
            throw new InvalidValueException("해당 아이디를 가진 예약이 존재하지 않습니다.");
        }
    }

    private void validateNotExistReservationTime(Long id) {
        if (!reservationTimeDao.exist(id)) {
            throw new InvalidValueException("예약 시간 아이디에 해당하는 예약 시간이 존재하지 않습니다.");
        }
    }
}
