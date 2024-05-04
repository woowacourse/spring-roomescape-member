package roomescape.reservation.domain;

import java.util.regex.Pattern;

public class Description {

    private static final Pattern DESCRIPTION_NAME_FORMAT = Pattern.compile("^[가-힣0-9\\s]+$");
    private static final int NAME_LENGTH = 50;
    private final String text;

    public Description(final String text) {
        validate(text);
        this.text = text;
    }

    private void validate(String text) {
        validateIsNull(text);
        validateIsBlank(text);
        validateFormat(text);
        validateLength(text);
    }

    private void validateIsNull(String name) {
        if (name == null) {
            throw new IllegalArgumentException("값을 입력하지 않았습니다.");
        }
    }

    private void validateIsBlank(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("테마명에 빈 문자열, 공백을 입력할 수 없습니다.");
        }
    }

    private void validateFormat(String name) {
        if (!DESCRIPTION_NAME_FORMAT.matcher(name).matches()) {
            throw new IllegalArgumentException("설명은 공백 포함 한글, 숫자만 입력 가능합니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException(String.format("설명은 %d자까지만 입력가능합니다.", NAME_LENGTH));
        }
    }


    public String getText() {
        return text;
    }
}
