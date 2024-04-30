package roomescape.domain;

public class ThemeName {

    private final String name;

    public ThemeName(String name) {
        validateName(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void validateName(String name) {
        if (name.isEmpty() || name.length() > 10) {
            throw new IllegalArgumentException("[ERROR] 테마이름은 1~10자만 가능합니다.");
        }
    }
}
