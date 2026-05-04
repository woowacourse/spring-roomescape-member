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
        Reservation savedReservation = reservationDao.save(Reservation.pending(name, date), timeId, themeId);
        return ReservationResponse.from(savedReservation);
    }

    public void delete(long reservationId) {
        reservationDao.delete(reservationId);
    }
}
