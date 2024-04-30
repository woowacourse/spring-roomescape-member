package roomescape.console.view;

import java.util.Collections;
import java.util.List;

public record Request(RequestMethod method, List<String> body) {
    public static final String REQUEST_DELIMITER = " ";
    public static final String BODY_DELIMITER = ",";
    public static final int METHOD_INDEX = 0;
    public static final int BODY_INDEX = 1;

    public static Request of(final String input) {
        final String[] requestInputParts = input.split(REQUEST_DELIMITER);
        final RequestMethod method = RequestMethod.of(requestInputParts[METHOD_INDEX]);
        validateInputParts(method, requestInputParts);
        final List<String> body = extractBody(method, requestInputParts);

        return new Request(method, body);
    }

    private static void validateInputParts(final RequestMethod method, final String[] requestInputParts) {
        if (method.hasBody() && requestInputParts.length < 2) {
            throw new IllegalArgumentException("요청 형식이 잘못되었습니다. 요청에 필요한 BODY를 모두 입력해주세요.");
        }
        if (!method.hasBody() && requestInputParts.length > 1) {
            throw new IllegalArgumentException("요청 형식이 잘못되었습니다. 해당 메소드에는 BODY가 필요하지 않습니다.");
        }
    }

    private static List<String> extractBody(final RequestMethod method, final String[] input) {
        if (method.hasBody()) {
            return List.of(input[BODY_INDEX].split(BODY_DELIMITER));
        }
        return List.of();
    }

    @Override
    public List<String> body() {
        return Collections.unmodifiableList(body);
    }
}
