package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.exception.InvalidReservationTimeException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAllWithTime();
    }

    public Reservation createReservation(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new InvalidReservationTimeException(timeId));
        Theme theme = themeRepository.findById(themeId);
        Long id = reservationRepository.save(name, date, timeId, themeId);

        return new Reservation(id, name, date, time, theme);
    }

    public void deleteReservation(Long id) {
        if (reservationRepository.deleteById(id) == 0) {
            throw new ReservationNotFoundException(id);
        }
    }
}
