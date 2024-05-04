package roomescape.domain;

public class UserName {
    private static final int MAX_NAME_LENGTH = 10;
    private final String value;

    public UserName(final String name) {
        validateName(name);
        this.value = name;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 잘못된 이름입니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 너무 긴 이름 입력입니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
