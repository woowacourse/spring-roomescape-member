package roomescape.service.reservation;

public final class ThemeDescription {
    private final String description;

    public ThemeDescription(final String description) {
        if (description == null || description.isBlank() || description.length() < 5 || description.length() > 200) {
            throw new IllegalArgumentException("테마 소개는 최소 5글자, 최대 200글자여야합니다.");
        }
        this.description = description;
    }
}
