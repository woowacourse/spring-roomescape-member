package roomescape.dto;

public record ThemeRequest(String name, String description, String url) {
    private static final int MIN_THEME_LENGTH = 1;
    private static final int MAX_THEME_LENGTH = 20;

    public ThemeRequest {
        validateName(name);
    }

    private static void validateName(String name) {
        if (name.isBlank() || name.length() > MAX_THEME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 테마 이름은 "+MIN_THEME_LENGTH+ "이상 "+MAX_THEME_LENGTH+"자 이하입니다.");
        }
    }
}
