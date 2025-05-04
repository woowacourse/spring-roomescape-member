package roomescape.reservation.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.reservation.domain.BookedCount;
import roomescape.theme.domain.Theme;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record ThemeToBookCountServiceResponse(
        Theme theme,
        BookedCount bookedCount
) {

    public ThemeToBookCountServiceResponse {
        validate(theme, bookedCount);
    }

    private void validate(final Theme theme, final BookedCount bookedCount) {
        Validator.of(ThemeToBookCountServiceResponse.class)
                .notNullField(Fields.theme, theme, Theme.domainName)
                .notNullField(Fields.bookedCount, bookedCount, BookedCount.domainName);
    }
}
