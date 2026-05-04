package roomescape.view;

import java.util.Scanner;

public class InputView {
    private static final String MENU_DELIMITER = "===============";
    private static final String INPUT_TYPE_ERROR = "잘못된 입력 형태입니다. 숫자만 입력해주세요.";

    private static final String MAIN_MENU_TITLE = "예약 관리 콘솔 모드";
    private static final String MAIN_MENU_OPTIONS = "1. 예약, 2: 예약 시간, 3: 종료";

    private static final String SUB_MENU_TITLE_FORMAT = "%s 관리 콘솔 모드";
    private static final String SUB_MENU_OPTIONS = "1: 전체 조회, 2: 추가, 3. 삭제, 4: 되돌아가기";

    private static final String PROMPT_NAME = "추가할 예약의 예약자 이름을 입력해주세요.";
    private static final String PROMPT_DATE = "추가할 예약의 날짜를 입력해주세요. (yyyy-MM-dd 형식 - 예: 2025-05-02)";
    private static final String PROMPT_TIME_ID = "추가할 예약의 시간 Id를 입력해주세요.";
    private static final String PROMPT_START_TIME = "추가할 시간을 입력해주세요. (HH:mm 형식 - 예: 16:03)";
    private static final String PROMPT_DELETE_ID = "삭제할 ID를 입력해주세요.";

    private static final Scanner scanner = new Scanner(System.in);

    public static int getMainMenu() {
        printMenuTitle(MENU_DELIMITER + MAIN_MENU_TITLE + MENU_DELIMITER);
        return askForInt(MAIN_MENU_OPTIONS);
    }

    public static int getSubMenu(String currentType) {
        printMenuTitle(String.format(SUB_MENU_TITLE_FORMAT, currentType));
        return askForInt(SUB_MENU_OPTIONS);
    }

    public static String getName() {
        return askForString(PROMPT_NAME);
    }

    public static String getDate() {
        return askForString(PROMPT_DATE);
    }

    public static Long getTimeId() {
        return askForLong(PROMPT_TIME_ID);
    }

    public static String getStartTime() {
        return askForString(PROMPT_START_TIME);
    }

    public static Long getDeleteId() {
        return askForLong(PROMPT_DELETE_ID);
    }

    private static void printMenuTitle(String title) {
        System.out.println(MENU_DELIMITER + title + MENU_DELIMITER);
    }

    private static String askForString(String message) {
        System.out.println(message);
        String input = scanner.nextLine();
        System.out.println();
        return input;
    }

    private static int askForInt(String message) {
        try {
            return Integer.parseInt(askForString(message).trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INPUT_TYPE_ERROR);
        }
    }

    private static Long askForLong(String message) {
        try {
            return Long.parseLong(askForString(message).trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INPUT_TYPE_ERROR);
        }
    }
}
