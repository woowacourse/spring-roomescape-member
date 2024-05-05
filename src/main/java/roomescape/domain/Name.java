package roomescape.domain;

public class Name {
    private final String value;

    public Name(final String name) {
        validateName(name);
        this.value = name;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 잘못된 이름입니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
