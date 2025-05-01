package roomescape.service.reservation;

public final class Theme {

    private final Long id;
    private final ThemeName name;
    private final ThemeDescription description;
    private final String thumbnail;

    public Theme(final Long id, final String name, final String description, final String thumbnail) {
        validateThumbnail(thumbnail);
        this.id = id;
        this.name = new ThemeName(name);
        this.description = new ThemeDescription(description);
        this.thumbnail = thumbnail;
    }

    private void validateThumbnail(final String thumbnail) {
        if (thumbnail == null) {
            throw new IllegalArgumentException("테마를 입력해야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getDescription() {
        return description.getDescription();
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
