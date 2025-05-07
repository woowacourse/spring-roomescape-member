package roomescape.model;

public class UserName {
    private final String name;

    public UserName(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalStateException("사용자의 이름이 NULL일 수 없습니다.");
        }
        if (name.isBlank() || name.length() > 10) {
            throw new IllegalStateException("사용자의 이름은 1자에서 10자 이내여야 합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
