package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;
import roomescape.domain.dto.ReservationRequest;
import roomescape.domain.dto.ReservationResponse;
import roomescape.exception.ErrorType;
import roomescape.exception.InvalidClientRequestException;
import roomescape.exception.ReservationFailException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;
import roomescape.repository.TimeDao;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final TimeDao timeDao;
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;

    public ReservationService(final
                              TimeDao timeDao, final ReservationDao reservationDao, final ThemeDao themeDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findEntireReservationList() {
        return reservationDao.findAll();
    }

    public ReservationResponse create(final ReservationRequest reservationRequest) {
        TimeSlot timeSlot = getTimeSlot(reservationRequest);
        Theme theme = getTheme(reservationRequest);
        validateDuplicatedReservation(reservationRequest.date(), timeSlot.getId(), theme.getId());
        Reservation newReservation = new Reservation(reservationRequest.name(), reservationRequest.date(), timeSlot, theme);
        Long reservationId = reservationDao.create(newReservation);
        return ReservationResponse.from(reservationId, newReservation);
    }

    private TimeSlot getTimeSlot(final ReservationRequest reservationRequest) {
        return timeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new InvalidClientRequestException(ErrorType.NOT_EXIST_TIME, "timeId", reservationRequest.timeId().toString()));
    }

    private Theme getTheme(final ReservationRequest reservationRequest) {
        return themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new InvalidClientRequestException(ErrorType.NOT_EXIST_TIME, "themeId", reservationRequest.themeId().toString()));
    }

    private void validateDuplicatedReservation(final LocalDate date, final Long timeId, final Long themeId) {
        if (reservationDao.isExists(date, timeId, themeId)) {
            throw new ReservationFailException("이미 예약이 등록되어 있습니다.");
        }
    }

    public void delete(final Long id) {
        reservationDao.delete(id);
    }
}
