package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dto.request.ReservationAddRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.reservation.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeService reservationTimeService, ThemeService themeService
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public ReservationResponse addReservation(ReservationAddRequest reservationAddRequest) {
        ReservationTimeResponse timeResponse = reservationTimeService.getTime(reservationAddRequest.timeId());
        ThemeResponse themeResponse = themeService.getTheme(reservationAddRequest.themeId());

        Reservation reservation = new Reservation(
                reservationAddRequest.name(),
                reservationAddRequest.date(),
                timeResponse.toReservationTime(),
                themeResponse.toTheme()
        );

        return ReservationResponse.from(reservationRepository.save(reservation));
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }

    public List<ReservationResponse> findReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
