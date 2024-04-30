package roomescape.console.view;

import java.util.Scanner;

public class InputView {
    private static final Scanner scanner = new Scanner(System.in);

    private InputView() {
    }

    public static String inputCommand() {
        System.out.println();
        System.out.print("> 필요한 관리 명령을 입력하세요. : ");
        return scanner.nextLine();
    }
}
