package roomescape.theme.domain;

import java.util.Objects;

public class Theme {
    private static final int MAX_NAME_LENGTH = 5;
    private static final int MAX_DESCRIPTION_LENGTH = 30;
    private static final int MAX_THUMBNAIL_LENGTH = 50;

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = Objects.requireNonNull(id, "id는 null일 수 없습니다.");
        this.name = Objects.requireNonNull(name, "name은 null일 수 없습니다.");
        this.description = Objects.requireNonNull(description, "description은 null일 수 없습니다.");
        this.thumbnail = Objects.requireNonNull(thumbnail, "thumbnail은 null일 수 없습니다.");

        validate();
    }

    private void validate() {
        if (name.trim().isEmpty()) {
            throw new IllegalStateException("name은 공백일 수 없습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalStateException("name은 " + MAX_NAME_LENGTH + "자 이내여야 합니다.");
        }

        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalStateException("description은 " + MAX_DESCRIPTION_LENGTH + "자 이내여야 합니다.");
        }

        if (thumbnail.length() > MAX_THUMBNAIL_LENGTH) {
            throw new IllegalStateException("thumbnail은 " + MAX_THUMBNAIL_LENGTH + "자 이내여야 합니다.");
        }
    }
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
}
