package roomescape.view;

import java.util.Scanner;
import roomescape.dto.reservation.ReservationCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;

public class InputView {

    private static final Scanner SCANNER = new Scanner(System.in);

    public Command readCommand() {
        String message = """
                명령어를 입력해주세요.
                1. 모든 예약 조회
                2. 예약 추가
                3. 예약 삭제
                4. 모든 예약 시간 조회
                5. 예약 시간 추가
                6. 예약 시간 삭제
                7. 종료
                """;
        System.out.println(message);
        String input = SCANNER.nextLine();
        return Command.from(input);
    }

    public ReservationCreateRequest createReservationCreatRequest() {
        System.out.println("예약자명을 입력해주세요.");
        String name = SCANNER.nextLine();
        System.out.println("예약일을 입력해주세요.");
        String date = SCANNER.nextLine();
        System.out.println("예약 시간 아이디를 입력해주세요.");
        long timeId = Long.parseLong(SCANNER.nextLine());
        return ReservationCreateRequest.of(name, date, timeId);
    }

    public Long readReservationId() {
        System.out.println("예약 아이디를 입력해주세요.");
        String input = SCANNER.nextLine();
        return Long.parseLong(input);
    }

    public ReservationTimeCreateRequest createReservationTimeCreatRequest() {
        System.out.println("예약 시간을 입력해주세요.");
        String startAt = SCANNER.nextLine();
        return ReservationTimeCreateRequest.from(startAt);
    }

    public Long readReservationTimeId() {
        System.out.println("예약 시간 아이디를 입력해주세요.");
        String input = SCANNER.nextLine();
        return Long.parseLong(input);
    }
}
