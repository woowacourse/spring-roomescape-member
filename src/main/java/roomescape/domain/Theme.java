package roomescape.domain;

import roomescape.exception.InvalidReservationException;

public class Theme {
    private static final long NO_ID = 0;
    private static final int MINIMUM_DESCRIPTION_LENGTH = 1;
    private static final int MAXIMUM_DESCRIPTION_LENGTH = 125;
    private static final String INVALID_DESCRIPTION_LENGTH = String.format("설명은 %d자 이상, %d자 이하여야 합니다.",
            MINIMUM_DESCRIPTION_LENGTH,
            MAXIMUM_DESCRIPTION_LENGTH);
    private static final int MINIMUM_THUMBNAIL_LENGTH = 1;
    private static final int MAXIMUM_THUMBNAIL_LENGTH = 250;
    private static final String INVALID_THUMBNAIL_LENGTH = String.format("썸네일 URL은 %d자 이상, %d자 이하여야 합니다.",
            MINIMUM_THUMBNAIL_LENGTH,
            MAXIMUM_THUMBNAIL_LENGTH);

    private long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnail;

    public Theme(long id, ThemeName name, String description, String thumbnail) {
        validateDescription(description);
        validateThumbnail(thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(final String name, final String description, final String thumbnail) {
        this(NO_ID, new ThemeName(name), description, thumbnail);
    }

    public Theme(long id, Theme theme) {
        this(id, theme.name, theme.description, theme.thumbnail);
    }

    public Theme(long id, String name, String description, String thumbnail) {
        this(id, new ThemeName(name), description, thumbnail);
    }

    private void validateDescription(final String description) {
        if (description.isEmpty() || description.length() > MAXIMUM_DESCRIPTION_LENGTH) {
            throw new InvalidReservationException(INVALID_DESCRIPTION_LENGTH);
        }
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
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
