package roomescape.console.view;

import java.util.Arrays;

public enum Command {
    READ_ALL_RESERVATIONS("1"),
    CREATE_RESERVATION("2"),
    DELETE_RESERVATION("3"),
    READ_ALL_RESERVATION_TIMES("4"),
    CREATE_RESERVATION_TIME("5"),
    DELETE_RESERVATION_TIME("6"),
    END("7");

    private final String value;

    Command(String value) {
        this.value = value;
    }

    public static Command from(String input) {
        return Arrays.stream(values())
                .filter(command -> command.value.equals(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당되는 명령어가 없습니다. 1~7의 정수를 입력하세요."));
    }
}
