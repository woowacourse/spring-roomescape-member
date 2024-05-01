package roomescape.console;

import java.util.Scanner;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.request.ReservationTimeRequest;

public class InputView {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String DELIMITER = ",";

    public static Command inputCommand() {
        System.out.println("input command. (ex - reservations/get)");
        String input = SCANNER.nextLine();
        return Command.findBy(input);
    }

    public static ReservationRequest inputReservationRequest() {
        System.out.println("input reservation request(name,date,timeId,themeId)."
                + " (ex - harry" + DELIMITER + "2024-05-11" + DELIMITER + "1" + DELIMITER + "1)");
        String input = SCANNER.nextLine();
        String[] split = input.split(DELIMITER);
        if (split.length == 4) {
            return new ReservationRequest(split[0], split[1], Long.parseLong(split[2]), Long.parseLong(split[3]));
        }
        throw new IllegalArgumentException("invalid reservation request: " + input);
    }

    public static long inputDeleteId() {
        System.out.println("input id to delete");
        String input = SCANNER.nextLine();
        return Long.parseLong(input);
    }

    public static ReservationTimeRequest inputReservationTimeRequest() {
        System.out.println("input reservation time request(startAt). (ex - 10:00)");
        String input = SCANNER.nextLine();
        return new ReservationTimeRequest(input);
    }
}
