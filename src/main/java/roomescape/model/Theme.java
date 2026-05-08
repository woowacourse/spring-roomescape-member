package roomescape.model;

import roomescape.dto.ThemeResponse;

public class Theme {
    private static final int MIN_THEME_NAME_LENGTH = 1;
    private static final int MAX_THEME_NAME_LENGTH = 20;

    private final Long id;
    private final String name;
    private final String description;
    private final String url;

    public Theme(Long id, String name, String description, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        validateName();
    }

    public static Theme from(ThemeResponse themeResponse) {
        return new Theme(themeResponse.id(), themeResponse.name(), themeResponse.description(), themeResponse.url());
    }

    private void validateName() {
        if (name.length() < MIN_THEME_NAME_LENGTH || name.length() > MAX_THEME_NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 테마 이름은 1자 이상 20자 이하입니다.");
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
