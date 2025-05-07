package roomescape.model;

public class ThemeName {
    private final String name;

    public ThemeName(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("테마 이름이 비어있습니다.");
        }
        if (name.isBlank() || name.length() > 10) {
            throw new IllegalArgumentException("테마 이름은 1자에서 10자 이내여야 합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
