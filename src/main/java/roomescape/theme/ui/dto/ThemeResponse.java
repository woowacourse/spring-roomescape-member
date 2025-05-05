package roomescape.theme.ui.dto;

import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeThumbnail;

import java.util.List;

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

    public static ThemeResponse from(final Theme domain) {
        return new ThemeResponse(
                domain.getId().getValue(),
                domain.getName().getValue(),
                domain.getDescription().getValue(),
                String.valueOf(domain.getThumbnail().getValue()));
    }

    public static List<ThemeResponse> from(final List<Theme> domains) {
        return domains.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private static void validate(final Long id,
                                 final String name,
                                 final String description,
                                 final String thumbnail) {
        Validator.of(ThemeResponse.class)
                .validateNotNull(Fields.id, id, ThemeId.domainName)
                .validateNotNull(Fields.name, name, ThemeName.domainName)
                .validateNotNull(Fields.description, description, ThemeDescription.domainName)
                .validateNotNull(Fields.thumbnail, thumbnail, ThemeThumbnail.domainName);
    }
}
