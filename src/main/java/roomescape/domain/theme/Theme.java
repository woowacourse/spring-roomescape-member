package roomescape.domain.theme;

import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String url;

    public Theme(Long id, String name, String description, String url) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_THEME_NAME);
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
