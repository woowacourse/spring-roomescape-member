package roomescape.global.error;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public enum TypeMismatchMessage {

    LONG(Long.class, "숫자 형식으로 입력해주세요."),
    INTEGER(Integer.class, "숫자 형식으로 입력해주세요."),
    LOCAL_DATE(LocalDate.class, "날짜 yyyy-MM-dd 형식으로 입력해주세요.");

    private static final String DEFAULT_MESSAGE = "잘못된 형식입니다.";

    private final Class<?> requiredType;
    private final String message;

    TypeMismatchMessage(Class<?> requiredType, String message) {
        this.requiredType = requiredType;
        this.message = message;
    }

    private String getMessage() {
        return this.message;
    }

    public static String from(Class<?> requiredType) {
        return Arrays.stream(TypeMismatchMessage.values())
            .filter(typeMismatchMessage -> Objects.equals(typeMismatchMessage.requiredType,
                requiredType))
            .map(TypeMismatchMessage::getMessage)
            .findFirst()
            .orElse(DEFAULT_MESSAGE);
    }
}
