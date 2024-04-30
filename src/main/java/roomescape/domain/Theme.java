package roomescape.domain;

import java.util.Objects;
import roomescape.exception.ExceptionType;
import roomescape.exception.RoomescapeException;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(long id, Theme theme) {
        this(id, theme.name, theme.description, theme.thumbnail);
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            //TODO : NAME_EMPYT 재사용 고민
            throw new RoomescapeException(ExceptionType.NAME_EMPTY);
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new RoomescapeException(ExceptionType.DESCRIPTION_EMPTY);
        }
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new RoomescapeException(ExceptionType.THUMBNAIL_EMPTY);
        }
    }

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    //todo : long 으로 변경
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Theme theme = (Theme) o;

        return Objects.equals(id, theme.id);
    }
}
