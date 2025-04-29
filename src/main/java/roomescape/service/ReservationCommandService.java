package roomescape.service;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;

@Service
public class ReservationCommandService {

    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;

    public ReservationCommandService(ReservationService reservationService,
                                     ReservationTimeService reservationTimeService) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
    }

    public List<ReservationResponse> getReservations() {
        return reservationService.getReservations();
    }

    public ReservationResponse createReservation(@Valid ReservationRequest reservationRequest) {
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest);
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.getReservationTime(
                reservationRequest.timeId());
        return ReservationResponse.of(reservationResponse, reservationTimeResponse);
    }

    public void delete(Long id) {
        reservationService.delete(id);
    }
}
