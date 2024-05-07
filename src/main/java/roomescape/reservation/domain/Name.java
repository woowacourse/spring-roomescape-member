package roomescape.reservation.domain;

public class Name {

    private final String name;

    public Name(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        validateIsNull(name);
        validateIsBlank(name);
    }

    private void validateIsNull(String name) {
        if (name == null) {
            throw new IllegalArgumentException("값을 입력하지 않았습니다.");
        }
    }

    private void validateIsBlank(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름에는 빈 문자열, 공백을 입력할 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }
}
