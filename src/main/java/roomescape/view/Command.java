package roomescape.view;

import java.util.Arrays;

public enum Command {

    READ_ALL_RESERVATIONS("1"),
    CREATE_RESERVATION("2"),
    DELETE_RESERVATION("3"),
    READ_ALL_RESERVATIONS_TIMES("4"),
    CREATE_RESERVATIONS_TIME("5"),
    DELETE_RESERVATIONS_TIME("6"),
    END("7");

    private final String value;

    Command(String value) {
        this.value = value;
    }

    public static Command from(String value) {
        return Arrays.stream(values())
                .filter(command -> command.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 값과 동일한 커맨드가 존재하지 않습니다."));
    }

    public boolean isEnd() {
        return END.equals(this);
    }
}
