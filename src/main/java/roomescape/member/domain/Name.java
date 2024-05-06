package roomescape.member.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public class Name {
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z가-힣0-9]+(?:\\s+[a-zA-Z가-힣0-9]+)*$");
    private static final int MAX_NAME_LENGTH = 10;
    private static final String INVALID_NAME_FORMAT = String.format("이름은 %d자 이하로 입력해주세요.", MAX_NAME_LENGTH);

    private final String value;

    public Name(final String name) {
        validate(name);
        this.value = name;
    }

    private void validate(final String name) {
        if (name.isEmpty() || name.length() > MAX_NAME_LENGTH || !PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(INVALID_NAME_FORMAT);
        }
    }

    public String getName() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Name name1 = (Name) o;
        return Objects.equals(getName(), name1.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Name{" +
                "name='" + value + '\'' +
                '}';
    }
}
