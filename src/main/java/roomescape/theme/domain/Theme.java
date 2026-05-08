package roomescape.theme.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import roomescape.theme.exception.InValidThemeException;
import roomescape.theme.exception.ThemeErrorCode;

@Getter
@EqualsAndHashCode(of = "id")
public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailUrl;

    private Theme(final Long id, final String name, final String description, final String thumbnailUrl) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    private void validateName(final String name) {
        List<String> errors = new ArrayList<>();

        if (name == null || name.isBlank()) {
            errors.add(ThemeErrorCode.THEME_NAME_NOT_BLANK.getMessage());
        }

        if(!errors.isEmpty()){
            throw new InValidThemeException(errors);
        }
    }

    public static Theme createNew(final String name, final String description, final String thumbnailUrl) {
        return new Theme(null, name, description, thumbnailUrl);
    }

    public static Theme of(final long id, final String name, final String description, final String thumbnailUrl) {
        return new Theme(id, name, description, thumbnailUrl);
    }

    public Theme withId(final long id) {
        return new Theme(id, name, description, thumbnailUrl);
    }

}
