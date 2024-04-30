package roomescape.model;

import java.util.regex.Pattern;

public class Name {

    private static final Pattern ONLY_NUMBER_PATTERN = Pattern.compile("^\\d+$");

    private final String value;

    public Name(final String value) {
        validateName(value);
        this.value = value;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어 있습니다.");
        }
        if (ONLY_NUMBER_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("이름에 숫자만 포함되어 있습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
