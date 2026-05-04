/*
package roomescape.controller;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.ReservationTime.ReservationTimeCommand;
import roomescape.dto.Reservation.AddReservationRequest;
import roomescape.dto.Reservation.ReservationResponse;
import roomescape.dto.ReservationTime.AddReservationTimeRequest;
import roomescape.dto.ReservationTime.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.service.RoomReservationService;
import roomescape.view.InputView;
import roomescape.view.OutputView;

@Component
@Profile("console")
public class RoomEscapeConsoleController implements CommandLineRunner {
    private final RoomReservationService reservationService;
    private final ReservationTimeService timeService;

    public RoomEscapeConsoleController(RoomReservationService reservationService, ReservationTimeService timeService) {
        this.reservationService = reservationService;
        this.timeService = timeService;
    }

    @Override
    public void run(String ...args) {
        while (true) {
            try {
                int mainSelection = InputView.getMainMenu();
                switch (mainSelection) {
                    case 1 -> manageReservations();
                    case 2 -> manageReservationTimes();
                    case 3 -> {return;}
                    default -> {}
                }
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }

    private void manageReservations() {
        int menu = InputView.getSubMenu("예약");
        switch (menu) {
            case 1 -> showAllReservations();
            case 2 -> addReservation();
            case 3 -> deleteReservation();
            default -> {}
        }
    }

    private void showAllReservations() {
        List<ReservationResponse> responses = reservationService.getAllReservation().stream()
                .map(ReservationResponse::from)
                .toList();
        OutputView.printReservations(responses);
    }

    private void addReservation() {
        String name = InputView.getName();
        String date = InputView.getDate();
        Long timeId = InputView.getTimeId();

        AddReservationRequest addReservationRequest = new AddReservationRequest(name, date, timeId);

        ReservationCommand reservationCommand = new ReservationCommand(addReservationRequest.name(), addReservationRequest.date(), addReservationRequest.timeId());
        ReservationResponse response = ReservationResponse.from(reservationService.addReservation(reservationCommand));
        OutputView.printAddedReservation(response);
    }

    private void deleteReservation() {
        Long id = InputView.getDeleteId();
        reservationService.deleteReservation(id);
        OutputView.printDeleteReservationComplete();
    }

    private void manageReservationTimes() {
        int menu = InputView.getSubMenu("예약 시간");
        switch (menu) {
            case 1 -> showAllTimes();
            case 2 -> addTime();
            case 3 -> deleteTime();
            default -> {}
        }
    }

    private void showAllTimes() {
        List<ReservationTimeResponse> responses = timeService.getAllReservationTime().stream()
                .map(ReservationTimeResponse::from)
                .toList();
        OutputView.printReservationTimes(responses);
    }

    private void addTime() {
        String startAt = InputView.getStartTime();
        AddReservationTimeRequest addReservationTimeRequest = new AddReservationTimeRequest(startAt);

        ReservationTimeCommand reservationTimeCommand = new ReservationTimeCommand(addReservationTimeRequest.startAt());
        ReservationTimeResponse response = ReservationTimeResponse.from(timeService.addReservationTime(reservationTimeCommand));
        OutputView.printAddedReservationTime(response);
    }

    private void deleteTime() {
        Long id = InputView.getDeleteId();
        timeService.deleteReservationTime(id);
        OutputView.printDeleteReservationTimeComplete();
    }
}
*/
