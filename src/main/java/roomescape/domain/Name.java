package roomescape.domain;

public class Name {

    private final String name;

    private Name(final String value) {
        validate(value);
        this.name = value;
    }

    public static Name from(String value) {
        return new Name(value);
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("이름을 입력해야 합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
