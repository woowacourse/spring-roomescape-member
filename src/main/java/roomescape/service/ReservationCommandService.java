package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ErrorMessage;
import roomescape.exception.custom.BadRequestException;
import roomescape.exception.custom.ConflictException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
@RequiredArgsConstructor
public class ReservationCommandService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationResponse create(String name, LocalDate date, long timeId, long themeId, LocalDateTime requestDateTime) {
        ReservationTime time = getReservationTime(timeId);

        validatePastDateTime(requestDateTime, date, time);
        validateNoDuplicateReservation(date, timeId, themeId);
        Reservation savedReservation = reservationDao.save(Reservation.pending(name, date), timeId, themeId);
        return ReservationResponse.from(savedReservation);
    }

    private ReservationTime getReservationTime(long timeId) {
        return reservationTimeDao.findByTimeId(timeId)
                .orElseThrow(() -> new BadRequestException(ErrorMessage.TIME_NOT_FOUND));
    }

    private void validatePastDateTime(LocalDateTime requestDateTime, LocalDate date, ReservationTime reservationTime) {
        if (requestDateTime.isAfter(LocalDateTime.of(date, reservationTime.startAt()))) {
            throw new BadRequestException(ErrorMessage.CANNOT_SELECT_PAST_DATETIME);
        }
    }

    private void validateNoDuplicateReservation(LocalDate date, long timeId, long themeId) {
        if (reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new ConflictException(ErrorMessage.DUPLICATE_RESERVATION);
        }
    }

    public void delete(long reservationId) {
        reservationDao.delete(reservationId);
    }
}
