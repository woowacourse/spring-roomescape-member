package roomescape.console;

import roomescape.dto.request.ReservationRequest;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;

public class ConsoleController {
    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;

    public ConsoleController(ReservationService reservationService,
                             ReservationTimeService reservationTimeService) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
    }

    public void run() {
        tryAgainWhenException(this::executeByInputCommand);
    }

    private void executeByInputCommand() {
        Command command;
        while ((command = InputView.inputCommand()) != Command.EXIT) {
            tryAgainWhenException(executeCommand(command));
        }
    }

    private Runnable executeCommand(Command command) {
        return switch (command) {
            case RESERVATION_GET -> this::printAllReservations;
            case RESERVATION_POST -> this::createReservation;
            case RESERVATION_DELETE -> this::deleteReservation;
            case RESERVATION_TIME_GET -> this::printAllReservationTimes;
            case RESERVATION_TIME_POST -> this::createReservationTime;
            case RESERVATION_TIME_DELETE -> this::deleteReservationTime;
            case EXIT -> throw new UnsupportedOperationException("unreachable statement.");
        };
    }

    private void printAllReservations() {
        OutputView.printAllReservations(reservationService.findAll());
    }

    private void createReservation() {
        ReservationRequest reservationRequest = InputView.inputReservationRequest();
        ReservationResponse response = reservationService.save(reservationRequest);
        OutputView.printReservationResponse(response);
    }

    private void deleteReservation() {
        long id = InputView.inputDeleteId();
        boolean deleted = reservationService.deleteById(id);
        OutputView.printDeleted(deleted);
    }

    private void printAllReservationTimes() {
        OutputView.printAllReservationTimes(reservationTimeService.findAll());
    }

    private void createReservationTime() {
        ReservationTimeRequest reservationTimeRequest = InputView.inputReservationTimeRequest();
        ReservationTimeResponse response = reservationTimeService.save(reservationTimeRequest);
        OutputView.printReservationTimeResponse(response);
    }

    private void deleteReservationTime() {
        long id = InputView.inputDeleteId();
        boolean deleted = reservationTimeService.deleteById(id);
        OutputView.printDeleted(deleted);
    }

    private void tryAgainWhenException(Runnable runnable) {
        try {
            runnable.run();
        } catch (RuntimeException e) {
            OutputView.printErrorMessage(e.getMessage());
            tryAgainWhenException(runnable);
        }
    }
}
