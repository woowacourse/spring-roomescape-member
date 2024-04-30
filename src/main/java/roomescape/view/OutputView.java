package roomescape.view;

import java.util.List;
import java.util.stream.Collectors;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservationtime.ReservationTimeResponse;

public class OutputView {

    private static final String RESERVATION_FORMAT = """
            <예약 정보>
            예약 아아디: %d
            예약자명: %s
            예약일 : %s
            예약 시간
                예약 시간 아이디: %d
                시작 시간: %s
            """;
    private static final String RESERVATION_TIME_FORMAT = """
            <예약 시간 정보>
            예약 시간 아아디: %d
            시작 시간: %s
            """;
    private static final String DELETED_MESSAGE = "성공적으로 삭제되었습니다.";

    public void printReservations(List<ReservationResponse> responses) {
        String result = responses.stream()
                .map(this::getFormattedReservation)
                .collect(Collectors.joining(System.lineSeparator()));
        System.out.println(result);
    }

    public void printReservation(ReservationResponse response) {
        String result = getFormattedReservation(response);
        System.out.println(result);
    }

    public void printReservationTimes(List<ReservationTimeResponse> responses) {
        String result = responses.stream()
                .map(this::getFormattedReservationTime)
                .collect(Collectors.joining(System.lineSeparator()));
        System.out.println(result);
    }

    public void printReservationTime(ReservationTimeResponse response) {
        String result = getFormattedReservationTime(response);
        System.out.println(result);
    }

    public void printSuccessDeleted() {
        System.out.println(DELETED_MESSAGE);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    private String getFormattedReservation(ReservationResponse response) {
        return RESERVATION_FORMAT.formatted(
                response.getId(),
                response.getName(),
                response.getDate(),
                response.getTime().getId(),
                response.getTime().getStartAt()
        );
    }

    private String getFormattedReservationTime(ReservationTimeResponse response) {
        return RESERVATION_TIME_FORMAT.formatted(
                response.getId(),
                response.getStartAt()
        );
    }
}
