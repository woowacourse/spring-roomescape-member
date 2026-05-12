package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.UnauthorizedActionException;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserReservationService {
    private final ReservationService reservationService;

    public UserReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public Reservation createReservation(String name, LocalDate date, long timeId, long themeId) {
        return reservationService.save(name, date, timeId, themeId);
    }

    @Transactional
    public void deleteReservation(long id, String name) {
        Reservation reservation = reservationService.findById(id);
        if (!reservation.getName().equals(name)) {
            throw new UnauthorizedActionException("예약자 이름이 일치하지 않습니다.");
        }
        reservationService.deleteById(id);
    }

    public List<Reservation> getReservations() {
        return reservationService.findAll();
    }
}
