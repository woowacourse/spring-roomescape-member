package roomescape.console.view;

import java.util.List;
import roomescape.core.dto.ReservationResponseDto;
import roomescape.core.dto.ReservationTimeResponseDto;

public class OutputView {
    private OutputView() {
    }

    public static void printWelcomeMessage() {
        final String message = """
                ============================
                방탈출 어드민에 오신 것을 환영합니다.
                ============================
                """;
        System.out.println(message);
    }

    public static void printCommands() {
        final String commands = """
                1. 예약 추가     ex) POST/reservations {name},{date},{time}
                2. 예약 목록 조회 ex) GET/reservations
                3. 예약 삭제     ex) DELETE/reservations {id}
                4. 시간 추가     ex) POST/times {startAt}
                5. 시간 목록 조회 ex) GET/times
                6. 시간 삭제     ex) DELETE/times {id}
                """;
        System.out.print(commands);
    }

    public static void printError(final String message) {
        System.err.println(message);
    }

    public static void printReservation(final ReservationResponseDto responseDto) {
        final String response = """
                {
                    "id": %d,
                    "name": "%s",
                    "date": "%s",
                    "time": {
                        "id": %d,
                        "startAt": "%s"
                    }
                }
                """;
        System.out.printf(response, responseDto.getId(), responseDto.getName(), responseDto.getDate(),
                responseDto.getTime().getId(), responseDto.getTime().getStartAt());
    }

    public static void printReservations(final List<ReservationResponseDto> reservationResponses) {
        if (reservationResponses.isEmpty()) {
            System.out.println("저장된 예약이 없습니다.");
            return;
        }
        for (final ReservationResponseDto reservationResponse : reservationResponses) {
            printReservation(reservationResponse);
        }
    }

    public static void printTime(final ReservationTimeResponseDto reservationTimeResponseDto) {
        final String response = """
                {
                    "id": %d,
                    "startAt": "%s"
                }
                """;
        System.out.printf(response, reservationTimeResponseDto.getId(), reservationTimeResponseDto.getStartAt());
    }

    public static void printTimes(final List<ReservationTimeResponseDto> reservationTimeResponses) {
        if (reservationTimeResponses.isEmpty()) {
            System.out.println("저장된 시간이 없습니다.");
            return;
        }
        for (final ReservationTimeResponseDto timeResponse : reservationTimeResponses) {
            printTime(timeResponse);
        }
    }

    public static void printReservationDeleteMessage(final Long id) {
        System.out.println("id: " + id + " 예약 삭제되었습니다.");
    }

    public static void printTimeDeleteMessage(final Long id) {
        System.out.println("id: " + id + " 시간 삭제되었습니다.");
    }
}
