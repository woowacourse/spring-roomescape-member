package roomescape.model;

import roomescape.dto.ThemeResponse;

public class Theme {
    private Long id;
    private final String name;
    private final String description;
    private final String url;
    private static final int MIN_THEME_LENGTH = 1;
    private static final int MAX_THEME_LENGTH = 20;

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

    private void validateName(String name){
        if (name.isBlank() || name.length() > MAX_THEME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 테마 이름은 "+MIN_THEME_LENGTH+ "이상 "+MAX_THEME_LENGTH+"자 이하입니다.");
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
