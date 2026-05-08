package roomescape.dto;

public record ThemeRequest(String name, String description, String url) {
    public ThemeRequest {
        validateName(name);
    }

    private static void validateName(String name) {
        if (name.length() < 1 || name.length() > 20) {
            throw new IllegalArgumentException("[ERROR] 테마 이름은 1자 이상 20자 이하입니다.");
        }
    }
}
