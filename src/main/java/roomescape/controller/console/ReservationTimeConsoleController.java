package roomescape.controller.console;

import java.util.List;
import org.springframework.stereotype.Controller;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.view.ReservationTimeView;

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
