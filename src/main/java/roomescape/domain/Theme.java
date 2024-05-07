package roomescape.domain;

import roomescape.exception.InvalidReservationException;

public class Theme {
    private static final long NO_ID = 0;
    private static final int MINIMUM_THUMBNAIL_LENGTH = 1;
    private static final int MAXIMUM_THUMBNAIL_LENGTH = 250;
    private static final String INVALID_THUMBNAIL_LENGTH = String.format("썸네일 URL은 %d자 이상, %d자 이하여야 합니다.",
            MINIMUM_THUMBNAIL_LENGTH,
            MAXIMUM_THUMBNAIL_LENGTH);

    private final long id;
    private final ThemeName name;
    private final Description description;
    private final String thumbnail;

    public Theme(long id, ThemeName name, Description description, String thumbnail) {
        validateThumbnail(thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(final String name, final String description, final String thumbnail) {
        this(NO_ID, new ThemeName(name), new Description(description), thumbnail);
    }

    public Theme(long id, Theme theme) {
        this(id, theme.name, theme.description, theme.thumbnail);
    }

    public Theme(long id, String name, String description, String thumbnail) {
        this(id, new ThemeName(name), new Description(description), thumbnail);
    }

    private void validateThumbnail(final String thumbnail) {
        if (thumbnail.isEmpty() || thumbnail.length() > MAXIMUM_THUMBNAIL_LENGTH) {
            throw new InvalidReservationException(INVALID_THUMBNAIL_LENGTH);
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
