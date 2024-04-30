package roomescape.controller.console;

import java.util.List;
import org.springframework.stereotype.Controller;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.view.ReservationTimeView;
import roomescape.view.ReservationView;

@Controller
public class ReservationConsoleController {
    private final ReservationView reservationView;
    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;

    public ReservationConsoleController(
            final ReservationView reservationView,
            final ReservationService reservationService,
            final ReservationTimeService reservationTimeService
    ) {
        this.reservationView = reservationView;
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
    }

    public void save() {
        try {
            List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();
            ReservationRequest reservationRequest = new ReservationRequest(
                    reservationView.readName(),
                    reservationView.readDate(),
                    ReservationTimeView.readIndexToReserve(reservationTimes),
                    0 // TODO: 수정
            );
            reservationService.save(reservationRequest);
            reservationView.printSuccessfullyAdded();
        } catch (IllegalStateException exception) {
            reservationView.printHasNotAnyReservationTime();
        }
    }

    public void getAll() {
        reservationView.printReservations(reservationService.findAll());
    }

    public void delete() {
        try {
            List<ReservationResponse> reservationResponses = reservationService.findAll();
            reservationService.deleteById(
                    reservationView.readIndexToDelete(reservationResponses)
            );
            reservationView.printSuccessfullyDeleted();
        } catch (final IllegalStateException exception) {
            reservationView.printHasNotAnyReservation();
        }
    }
}
