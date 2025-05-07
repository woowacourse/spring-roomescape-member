package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservedChecker;

@Service
public class ReservationService {
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;
    private final ReservationRepository reservationRepository;
    private final ReservedChecker reservedChecker;

    public ReservationService(ReservationTimeService reservationTimeService, ThemeService themeService,
                              ReservationRepository reservationRepository, ReservedChecker reservedChecker) {
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
        this.reservationRepository = reservationRepository;
        this.reservedChecker = reservedChecker;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    public Reservation addReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeService.getReservationTimeById(reservationRequest.timeId());
        Theme theme = themeService.getThemeById(reservationRequest.themeId());
        Reservation reservationWithNoId = Reservation.createWithNoId(reservationRequest, reservationTime, theme);

        validateUniqueReservation(reservationRequest.date(), reservationRequest.timeId(),
                reservationRequest.themeId());
        return reservationRepository.addReservation(reservationWithNoId);
    }

    private void validateUniqueReservation(LocalDate reservationDate, Long timeId, Long themeId) {
        if (reservedChecker.contains(reservationDate, timeId, themeId)) {
            throw new IllegalArgumentException("Reservation already exists");
        }
    }

    public void deleteReservation(long id) {
        reservationRepository.deleteReservation(id);
    }

}
