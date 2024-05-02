package roomescape.console.controller;

import java.util.List;
import roomescape.console.view.OutputView;
import roomescape.core.dto.ReservationTimeRequestDto;
import roomescape.core.service.ReservationTimeService;

public class ReservationTimeConsoleController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeConsoleController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    public void save(final ReservationTimeRequestDto reservationTimeRequestDto) {
        OutputView.printTime(reservationTimeService.create(reservationTimeRequestDto));
    }

    public void findAll() {
        OutputView.printTimes(reservationTimeService.findAll());
    }

    public void delete(final List<String> body) {
        final long reservationTimeId = Long.parseLong(body.get(0));
        reservationTimeService.delete(reservationTimeId);
        OutputView.printTimeDeleteMessage(reservationTimeId);
    }
}
