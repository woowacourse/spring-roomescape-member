package roomescape.theme.domain;

import io.micrometer.common.util.StringUtils;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.ValidateException;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(final String name, final String description, final String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(final Long id, final Theme theme) {
        this(id, theme.name, theme.description, theme.thumbnail);
    }

    public Theme(final Long id, final String name, String description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;

        validateBlank();
    }

    private void validateBlank() {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(description) || StringUtils.isBlank(thumbnail)) {
            throw new ValidateException(ErrorType.THEME_REQUEST_DATA_BLANK,
                    String.format("테마(Theme) 생성에 유효하지 않은 값(null OR 공백)이 입력되었습니다. [values : %s]", this));
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

    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
