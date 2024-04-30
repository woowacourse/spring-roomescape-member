package roomescape.controller.console;

import static roomescape.view.Command.READ_ALL_RESERVATIONS;

import java.util.List;
import java.util.function.Supplier;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.view.Command;
import roomescape.view.InputView;
import roomescape.view.OutputView;

public class RoomEscapeConsoleController {

    private final InputView inputView;
    private final OutputView outputView;
    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;

    public RoomEscapeConsoleController(InputView inputView,
                                       OutputView outputView,
                                       ReservationService reservationService,
                                       ReservationTimeService reservationTimeService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
    }

    public void run() {
        Command command = READ_ALL_RESERVATIONS;
        while (!command.isEnd()) {
            command = retryUntilSuccess(this::readCommandAndExecute);
        }
    }

    private Command readCommandAndExecute() {
        Command newCommand = inputView.readCommand();
        execute(newCommand);
        return newCommand;
    }

    private void execute(Command command) {
        switch (command) {
            case READ_ALL_RESERVATIONS -> printReservations();
            case READ_ALL_RESERVATIONS_TIMES -> printReservationTimes();
            case CREATE_RESERVATION -> createReservation();
            case CREATE_RESERVATIONS_TIME -> createReservationTime();
            case DELETE_RESERVATION -> deleteReservation();
            case DELETE_RESERVATIONS_TIME -> deleteReservationTime();
            default -> throw new UnsupportedOperationException("지원하지 않는 명령입니다. : " + command);
        }
    }

    private void printReservations() {
        List<ReservationResponse> results = reservationService.findAll();
        outputView.printReservations(results);
    }

    private void printReservationTimes() {
        List<ReservationTimeResponse> results = reservationTimeService.findAll();
        outputView.printReservationTimes(results);
    }

    private void createReservation() {
        ReservationCreateRequest createRequest = inputView.createReservationCreatRequest();
        ReservationResponse result = reservationService.add(createRequest);
        outputView.printReservation(result);
    }

    private void createReservationTime() {
        ReservationTimeCreateRequest createRequest = inputView.createReservationTimeCreatRequest();
        ReservationTimeResponse result = reservationTimeService.add(createRequest);
        outputView.printReservationTime(result);
    }

    private void deleteReservation() {
        Long id = inputView.readReservationId();
        reservationService.delete(id);
        outputView.printSuccessDeleted();
    }

    private void deleteReservationTime() {
        Long id = inputView.readReservationTimeId();
        reservationTimeService.delete(id);
        outputView.printSuccessDeleted();
    }

    private <T> T retryUntilSuccess(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                outputView.printMessage((e.getMessage()));
            }
        }
    }
}
