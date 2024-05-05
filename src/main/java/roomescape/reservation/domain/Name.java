package roomescape.reservation.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public class Name {
    private static final int MAX_NAME_LENGTH = 10;
    private static final String REGEX = "^[가-힣a-zA-Z]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private final String value;

    public Name(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String name) {
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름은 1자 이상 10자 이하로 입력해야 합니다.");
        }

        if (!PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("이름은 한글 또는 영어만 입력가능합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
