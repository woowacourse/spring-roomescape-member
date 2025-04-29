package roomescape.theme.ui.dto;

import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;

@FieldNameConstants
public record ThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public ThemeResponse {
        validate(id, name, description, thumbnail);
    }

    private static void validate(final Long id, final String name, final String description, final String thumbnail) {
        Validator.of(ThemeResponse.class)
                .notNullField(Fields.id, id)
                .notNullField(Fields.name, name)
                .notNullField(Fields.description, description)
                .notNullField(Fields.thumbnail, thumbnail);
    }
}
