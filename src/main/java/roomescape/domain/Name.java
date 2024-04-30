package roomescape.domain;

public class Name {
    private final String name;

    public Name(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("이름은 null이거나 공백만 포함할 수 없다.");
        }
    }

    public String getValue() {
        return name;
    }
}
