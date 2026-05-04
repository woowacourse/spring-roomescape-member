package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public Reservation saveReservation(String name, LocalDate date, Long reservationTimeId, Long themeId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationTimeId);
        Theme theme = themeRepository.findById(themeId);
        Reservation transientReservation = Reservation.transientOf(name, date, reservationTime, theme);
        return reservationRepository.save(transientReservation);
    }

    public void removeReservation(long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    public Reservation findReservation(long reservationId) {
        return reservationRepository.findById(reservationId);
    }
}
