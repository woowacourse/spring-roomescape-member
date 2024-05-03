package roomescape.console.controller;

import java.util.List;
import roomescape.console.view.InputView;
import roomescape.console.view.OutputView;
import roomescape.console.view.Request;
import roomescape.console.view.RequestBodyMapper;
import roomescape.core.dto.ReservationRequestDto;
import roomescape.core.dto.ReservationTimeRequestDto;

public class FrontController {
    private static final String EXIT = "exit";

    private final ReservationConsoleController reservationConsoleController;
    private final ReservationTimeConsoleController reservationTimeConsoleController;
    private final RequestBodyMapper requestBodyMapper;

    public FrontController(final ReservationConsoleController reservationConsoleController,
                           final ReservationTimeConsoleController reservationTimeConsoleController,
                           final RequestBodyMapper requestBodyMapper) {
        this.reservationConsoleController = reservationConsoleController;
        this.reservationTimeConsoleController = reservationTimeConsoleController;
        this.requestBodyMapper = requestBodyMapper;
    }

    public void run() {
        OutputView.printWelcomeMessage();
        OutputView.printCommands();
        String command = InputView.inputCommand();

        while (!command.equals(EXIT)) {
            parseToRequest(command);
            command = InputView.inputCommand();
        }
    }

    private void parseToRequest(final String command) {
        try {
            Request request = Request.of(command);
            findDelegate(request);
        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
        }
    }

    private void findDelegate(final Request request) {
        switch (request.method()) {
            case POST_RESERVATION -> saveReservation(request);
            case GET_RESERVATION -> reservationConsoleController.findAll();
            case DELETE_RESERVATION -> reservationConsoleController.delete(request.body());
            case POST_TIME -> saveReservationTime(request);
            case GET_TIME -> reservationTimeConsoleController.findAll();
            case DELETE_TIME -> reservationTimeConsoleController.delete(request.body());
        }
    }

    private void saveReservation(final Request request) {
        final List<String> body = request.body();
        final ReservationRequestDto reservationRequestDto = requestBodyMapper.mapReservationRequest(body);
        reservationConsoleController.save(reservationRequestDto);
    }

    private void saveReservationTime(final Request request) {
        final List<String> body = request.body();
        final ReservationTimeRequestDto reservationTimeRequestDto = requestBodyMapper.mapReservationTimeRequest(body);
        reservationTimeConsoleController.save(reservationTimeRequestDto);
    }
}
