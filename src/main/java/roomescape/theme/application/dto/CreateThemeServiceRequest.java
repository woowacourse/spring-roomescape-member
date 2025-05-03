package roomescape.theme.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeThumbnail;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record CreateThemeServiceRequest(
        String name,
        String description,
        String thumbnail
) {

    public CreateThemeServiceRequest {
        validate(name, description, thumbnail);
    }

    public Theme toDomain() {
        return Theme.withoutId(
                ThemeName.from(name),
                ThemeDescription.from(description),
                ThemeThumbnail.from(thumbnail)
        );
    }

    private void validate(final String name, final String description, final String thumbnail) {
        Validator.of(CreateThemeServiceRequest.class)
                .notNullField(CreateThemeServiceRequest.Fields.name, name)
                .notNullField(CreateThemeServiceRequest.Fields.description, description)
                .notNullField(CreateThemeServiceRequest.Fields.thumbnail, thumbnail);
    }
}
