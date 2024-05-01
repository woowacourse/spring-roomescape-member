package roomescape.console;

import java.util.List;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;

public class OutputView {
    public static void printErrorMessage(String errorMessage) {
        System.out.println("[ERROR] " + errorMessage);
    }

    public static void printAllReservations(List<ReservationResponse> reservationResponses) {
        System.out.println("reservation time responses:");
        for (ReservationResponse reservationResponse : reservationResponses) {
            System.out.println(reservationResponse);
        }
    }

    public static void printReservationResponse(ReservationResponse response) {
        System.out.println(response);
    }

    public static void printDeleted(boolean deleted) {
        String deleteResult = "failed";
        if (deleted) {
            deleteResult = "success";
        }
        System.out.println("delete " + deleteResult);
    }

    public static void printAllReservationTimes(
            List<ReservationTimeResponse> reservationTimeResponses) {
        System.out.println("reservation time responses:");
        for (ReservationTimeResponse reservationTimeResponse : reservationTimeResponses) {
            System.out.println(reservationTimeResponse);
        }
    }

    public static void printReservationTimeResponse(ReservationTimeResponse response) {
        System.out.println(response);
    }
}
