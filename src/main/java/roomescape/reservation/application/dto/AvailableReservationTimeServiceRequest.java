package roomescape.reservation.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.utils.Validator;
import roomescape.theme.domain.ThemeId;

import java.time.LocalDate;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record AvailableReservationTimeServiceRequest(
        LocalDate date,
        ThemeId themeId
) {

    public AvailableReservationTimeServiceRequest {
        validate(date, themeId);
    }

    private void validate(final LocalDate date, final ThemeId themeId) {
        Validator.of(AvailableReservationTimeServiceRequest.class)
                .notNullField(Fields.date, date)
                .notNullField(Fields.themeId, themeId);
    }
}
