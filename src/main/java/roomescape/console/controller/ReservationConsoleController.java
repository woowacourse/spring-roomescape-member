package roomescape.console.controller;

import java.util.List;
import roomescape.console.view.OutputView;
import roomescape.core.dto.ReservationRequestDto;
import roomescape.core.service.ReservationService;

public class ReservationConsoleController {

    private final ReservationService reservationService;

    public ReservationConsoleController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void save(final ReservationRequestDto reservationRequestDto) {
        OutputView.printReservation(reservationService.create(reservationRequestDto));
    }

    public void findAll() {
        OutputView.printReservations(reservationService.findAll());
    }

    public void delete(final List<String> body) {
        final long reservationId = Long.parseLong(body.get(0));
        reservationService.delete(reservationId);
        OutputView.printReservationDeleteMessage(reservationId);
    }
}
