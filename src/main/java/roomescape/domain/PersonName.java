package roomescape.domain;

public class PersonName {

    private final String name;

    private PersonName(final String value) {
        validate(value);
        this.name = value;
    }

    public static PersonName from(String value) {
        return new PersonName(value);
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
