package roomescape.model;

import roomescape.exception.BadRequestException;
import roomescape.exception.ErrorCode;

public class Theme {

    private static final int MAX_THEME_NAME_LENGTH = 20;
    private static final int MAX_THEME_DESCRIPTION_LENGTH = 1000;

    private final Long id;
    private final String name;
    private final String description;
    private final String url;

    public Theme(Long id, String name, String description, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        validateAll();
    }

    private void validateAll() {
        validateName();
        validateDescription();
        validateUrl();
    }

    private void validateName() {
        if (name == null || name.isBlank()) {
            throw new BadRequestException(ErrorCode.THEME_NAME_BLANK);
        }

        if (name.length() > MAX_THEME_NAME_LENGTH) {
            throw new BadRequestException(ErrorCode.THEME_NAME_LENGTH_INVALID);
        }
    }

    private void validateDescription() {
        if (description == null || description.isBlank()) {
            throw new BadRequestException(ErrorCode.THEME_DESCRIPTION_BLANK);
        }
        if (description.length() > MAX_THEME_DESCRIPTION_LENGTH) {
            throw new BadRequestException(ErrorCode.THEME_DESCRIPTION_LENGTH_INVALID);
        }
    }

    private void validateUrl() {
        if (url == null || url.isBlank()) {
            throw new BadRequestException(ErrorCode.THEME_URL_BLANK);
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

    public String getUrl() {
        return url;
    }
}
