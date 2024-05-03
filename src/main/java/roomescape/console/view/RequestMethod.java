package roomescape.console.view;

import java.util.Arrays;

public enum RequestMethod {
    POST_RESERVATION("POST/reservations", true),
    GET_RESERVATION("GET/reservations", false),
    DELETE_RESERVATION("DELETE/reservations", true),
    POST_TIME("POST/times", true),
    GET_TIME("GET/times", false),
    DELETE_TIME("DELETE/times", true);

    private final String method;
    private final boolean hasBody;

    RequestMethod(final String value, final boolean hasBody) {
        this.method = value;
        this.hasBody = hasBody;
    }

    public static RequestMethod of(final String value) {
        return Arrays.stream(values())
                .filter(request -> request.method.equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 명령어입니다."));
    }

    public boolean hasBody() {
        return hasBody;
    }
}
