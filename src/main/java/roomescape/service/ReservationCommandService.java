package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ErrorMessage;
import roomescape.exception.PastReservationTimeException;
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
                .orElseThrow(() -> new IllegalStateException("해당하는 ID의 시간이 존재하지 않습니다."));
    }

    private void validatePastDateTime(LocalDateTime requestDateTime, LocalDate date, ReservationTime reservationTime) {
        if (requestDateTime.isAfter(LocalDateTime.of(date, reservationTime.startAt()))) {
            throw new PastReservationTimeException(ErrorMessage.CANNOT_SELECT_PAST_DATETIME);
        }
    }

    private void validateNoDuplicateReservation(LocalDate date, long timeId, long themeId) {
        if (reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalStateException("이미 중복된 예약이 존재합니다.");
        }
    }

    public void delete(long reservationId) {
        reservationDao.delete(reservationId);
    }
}
