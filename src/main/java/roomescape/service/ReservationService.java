package roomescape.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ThemeResponse;
import roomescape.dto.TimeResponse;
import roomescape.model.Reservation;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeService timeService;
    private final ThemeService themeService;

    public ReservationService(ReservationRepository reservationRepository, TimeService timeService,
                              ThemeService themeService) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
        this.themeService = themeService;
    }

    public List<ReservationResponse> readReservation() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    public void removeReservation(Long id) {
        reservationRepository.removeById(id);
    }

    public ReservationResponse registerReservation(ReservationRequest reservationRequest) {
        TimeResponse timeResponse = timeService.findById(reservationRequest.getTimeId());
        ThemeResponse themeResponse = themeService.findById(reservationRequest.getThemeId());
        Reservation reservation = reservationRepository.register(reservationRequest, timeResponse, themeResponse);
        return ReservationResponse.from(reservation);
    }

}
