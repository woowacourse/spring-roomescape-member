package roomescape.service.reservation;

public final class ThemeName {

    private final String name;

    public ThemeName(final String name) {
        validateName(name);
        this.name = name;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank() || name.length() > 20) {
            throw new IllegalArgumentException("테마 이름은 최소 1글자, 최대 20글자여야합니다.");
        }
    }
}
