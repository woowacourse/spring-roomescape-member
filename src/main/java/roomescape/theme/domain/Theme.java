package roomescape.theme.domain;

import java.util.Objects;
import roomescape.exception.custom.InvalidInputException;

public class Theme {

    private static final int NON_SAVED_STATUS = 0;
    private static final int MAX_LENGTH = 255;

    private final long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(final long id, final String name, final String description, final String thumbnail) {
        validateInvalidInput(name, description, thumbnail);

        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(final String name, final String description, final String thumbnail) {
        this(NON_SAVED_STATUS, name, description, thumbnail);
    }

    public Theme(final long id, final Theme savedRoomTheme) {
        this(id, savedRoomTheme.getName(), savedRoomTheme.getDescription(), savedRoomTheme.getThumbnail());
    }

    private void validateInvalidInput(final String name, final String description, final String thumbnail) {
        validateNotNull(name, description, thumbnail);
        validateValidLength(name, description, thumbnail);
    }

    private void validateNotNull(final String name, final String description, final String thumbnail) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new InvalidInputException("테마 명은 빈 값이 입력될 수 없습니다");
        }
        if (Objects.isNull(description) || description.isBlank()) {
            throw new InvalidInputException("테마 상세 설명은 빈 값이 입력될 수 없습니다");
        }
        if (Objects.isNull(thumbnail) || thumbnail.isBlank()) {
            throw new InvalidInputException("썸네일 주소는 빈 값이 입력될 수 없습니다");
        }
    }

    private void validateValidLength(final String name, final String description, final String thumbnail) {
        if (name.length() > MAX_LENGTH) {
            throw new InvalidInputException("테마 명은 255자를 초과할 수 없습니다.");
        }
        if (description.length() > MAX_LENGTH) {
            throw new InvalidInputException("테마 상세 설명은 255자를 초과할 수 없습니다.");
        }
        if (thumbnail.length() > MAX_LENGTH) {
            throw new InvalidInputException("썸네일 주소는 255자를 초과할 수 없습니다.");
        }
    }

    public long getId() {
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
