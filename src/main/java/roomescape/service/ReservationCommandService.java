package roomescape.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.ReservationDao;

@Service
@RequiredArgsConstructor
public class ReservationCommandService {

    private final ReservationDao reservationDao;

    public ReservationResponse create(String name, LocalDate date, long timeId, long themeId) {
        validateNoDuplicateReservation(date, timeId, themeId);
        Reservation savedReservation = reservationDao.save(Reservation.pending(name, date), timeId, themeId);
        return ReservationResponse.from(savedReservation);
    }

    private void validateNoDuplicateReservation(LocalDate date, long timeId, long themeId) {
        if(reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new IllegalStateException("이미 중복된 예약이 존재합니다.");
        }
    }

    public void delete(long reservationId) {
        reservationDao.delete(reservationId);
    }
}
