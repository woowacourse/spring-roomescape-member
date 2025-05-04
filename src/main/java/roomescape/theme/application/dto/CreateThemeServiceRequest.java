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
        ThemeName name,
        ThemeDescription description,
        ThemeThumbnail thumbnail
) {

    public CreateThemeServiceRequest {
        validate(name, description, thumbnail);
    }

    public Theme toDomain() {
        return Theme.withoutId(
                name,
                description,
                thumbnail
        );
    }

    private void validate(final ThemeName name, final ThemeDescription description, final ThemeThumbnail thumbnail) {
        Validator.of(CreateThemeServiceRequest.class)
                .notNullField(CreateThemeServiceRequest.Fields.name, name, ThemeName.domainName)
                .notNullField(CreateThemeServiceRequest.Fields.description, description, ThemeDescription.domainName)
                .notNullField(CreateThemeServiceRequest.Fields.thumbnail, thumbnail, ThemeThumbnail.domainName);
    }
}
