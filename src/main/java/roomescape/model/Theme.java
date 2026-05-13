package roomescape.model;

import roomescape.dto.ThemeResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;

public class Theme {
    private static final int MAX_THEME_LENGTH = 20;
    private final String name;
    private final String description;
    private final String url;
    private final Long id;

    public Theme(Long id, String name, String description, String url) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public static Theme from(ThemeResponse themeResponse) {
        return new Theme(themeResponse.id(), themeResponse.name(), themeResponse.description(), themeResponse.url());
    }

    private void validateName(String name) {
        if (name.isBlank() || name.length() > MAX_THEME_LENGTH) {
            throw new RoomescapeException(ErrorCode.THEME_WRONG_NAME);
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
