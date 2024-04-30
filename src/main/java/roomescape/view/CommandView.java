package roomescape.view;

import java.util.Scanner;
import roomescape.controller.console.command.Command;

public class CommandView {
    private final Scanner scanner = new Scanner(System.in);

    public Command readCommand() {
        System.out.printf(
                "%n[INFO] 실행할 메뉴를 선택해주세요. 종료를 원하시면 0을 입력해주세요.%n"
                        + "1. 예약 시간 추가%n"
                        + "2. 예약 시간 삭제%n"
                        + "3. 예약 시간 조회%n"
                        + "4. 예약 추가%n"
                        + "5. 예약 삭제%n"
                        + "6. 예약 조회%n"
        );
        try {
            return new Command(Integer.parseInt(scanner.nextLine()));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("[ERROR] 올바른 숫자를 입력해주세요.");
        }
    }
}
