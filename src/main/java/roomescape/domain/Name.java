package roomescape.domain;

public class Name {
    private final String name;

    public Name(String name) {
        validateEmpty(name);
        this.name = name;
    }

    public void validateEmpty(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름에 null 혹은 빈문자열을 입력할 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }
}
