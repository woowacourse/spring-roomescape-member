package roomescape.domain;

public class Name {

    private final String name;

    public Name(String name) {
        validateNonBlank(name);
        this.name = name;
    }

    private void validateNonBlank(String name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("이름은 비어있을 수 없습니다.");
        }
    }
}
