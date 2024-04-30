package roomescape.domain.user;

import java.util.regex.Pattern;

public class UserName {
    private static final int MIN_NAME_LENGTH = 2;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z]*$");

    private final String name;

    public UserName(String name) {
        validateBlank(name);
        validateLength(name);
        validatePattern(name);
        this.name = name;
    }

    private void validateBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException(String.format("이름은 %d글자 이상이어야 합니다.", MIN_NAME_LENGTH));
        }
    }

    private void validatePattern(String name) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("이름은 한글, 영어만 가능합니다.");
        }
    }

    public String getValue() {
        return this.name;
    }
}
