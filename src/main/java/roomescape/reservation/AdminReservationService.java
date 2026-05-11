package roomescape.reservation;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AdminReservationService {
    private final ReservationService reservationService;

    public AdminReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public Reservation createForceReservation(String name, LocalDate date, Long timeId, Long themeId) {
        return reservationService.save(name, date, timeId, themeId);
    }

    public void forceDeleteReservation(long id) {
        reservationService.deleteById(id);
    }
}
