package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.ReservationTime;
import roomescape.theme.dao.ThemeDao;
import roomescape.reservation.dao.TimeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.response.ReservationTimeInfosResponse;
import roomescape.reservation.dto.response.ReservationsResponse;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.DataDuplicateException;
import roomescape.global.exception.model.ValidateException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(final ReservationDao reservationDao,
                              final TimeDao timeDao,
                              final ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public ReservationsResponse findAllReservations() {
        List<ReservationResponse> response = reservationDao.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();

        return new ReservationsResponse(response);
    }

    public ReservationTimeInfosResponse findReservationsByDateAndThemeId(final LocalDate date, final Long themeId) {
        return timeDao.findByDateAndThemeId(date, themeId);
    }

    public void removeReservationById(final Long id) {
        reservationDao.deleteById(id);
    }

    public ReservationResponse addReservation(final ReservationRequest reservationRequest) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate requestDate = reservationRequest.date();
        ReservationTime requestReservationTime = timeDao.findById(reservationRequest.timeId());
        Theme theme = themeDao.findById(reservationRequest.themeId());

        validateDateAndTime(requestDate, requestReservationTime, now);
        validateReservationDuplicate(reservationRequest, theme);

        Reservation savedReservation = reservationDao.insert(reservationRequest.toReservation(requestReservationTime, theme));

        return ReservationResponse.from(savedReservation);
    }

    private void validateDateAndTime(final LocalDate requestDate, final ReservationTime requestReservationTime, final LocalDateTime now) {
        if (isReservationInPast(requestDate, requestReservationTime, now)) {
            throw new ValidateException(ErrorType.RESERVATION_PERIOD_IN_PAST,
                    String.format("지난 날짜나 시간은 예약이 불가능합니다. [now: %s %s | request: %s %s]",
                            now.toLocalDate(), now.toLocalTime(), requestDate, requestReservationTime.getStartAt()));
        }
    }

    private boolean isReservationInPast(final LocalDate requestDate, final ReservationTime requestReservationTime, final LocalDateTime now) {
        LocalDate today = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();

        if (requestDate.isBefore(today)) {
            return true;
        }
        if (requestDate.isEqual(today) && requestReservationTime.getStartAt().isBefore(nowTime)) {
            return true;
        }
        return false;
    }

    private void validateReservationDuplicate(final ReservationRequest reservationRequest, final Theme theme) {
        List<Reservation> duplicateTimeReservations = reservationDao.findByTimeIdAndDateAndThemeId(
                reservationRequest.timeId(), reservationRequest.date(), theme.getId());

        if (duplicateTimeReservations.size() > 0) {
            throw new DataDuplicateException(ErrorType.RESERVATION_DUPLICATED,
                    String.format("이미 해당 날짜/시간/테마에 예약이 존재합니다. [values: %s]", reservationRequest));
        }
    }
}
