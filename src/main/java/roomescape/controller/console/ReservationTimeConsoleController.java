package roomescape.controller.console;

import org.springframework.stereotype.Controller;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.view.ReservationTimeView;

import java.util.List;

@Controller
public class ReservationTimeConsoleController {
    private final ReservationTimeView reservationTimeView;
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeConsoleController(
            final ReservationTimeView reservationTimeView,
            final ReservationTimeService reservationTimeService
    ) {
        this.reservationTimeView = reservationTimeView;
        this.reservationTimeService = reservationTimeService;
    }

    public void save() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(
                reservationTimeView.readStartAt()
        );
        reservationTimeService.save(reservationTimeRequest);
        reservationTimeView.printSuccessfullyAdded();
    }

    public void getAll() {
        ReservationTimeView.printReservationTimes(reservationTimeService.getAll());
    }

    public void delete() {
        try {
            List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.findAll();
            reservationTimeService.deleteById(
                    reservationTimeView.readIndexToDelete(reservationTimeResponses)
            );
            reservationTimeView.printSuccessfullyDeleted();
        } catch (IllegalStateException exception) {
            reservationTimeView.printHasNotAnyReservationTimeToDelete();
        }
    }
}
