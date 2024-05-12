package roomescape.domain;

public record ThemeName(String value) {

    private static final int MAX_THEME_NAME_LENGTH = 100;

    public ThemeName {
        if (value.isBlank()) {
            throw new IllegalArgumentException("제목은 공백을 제외한 1글자 이상이어야 합니다.");
        }

        if (value.length() > MAX_THEME_NAME_LENGTH) {
            String message = String.format("제목은 %d글자를 넘을 수 없습니다.", MAX_THEME_NAME_LENGTH);
            throw new IllegalArgumentException(message);
        }
    }
}
