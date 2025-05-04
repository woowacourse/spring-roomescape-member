package roomescape.reservation.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.domain.ThemeId;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record AvailableReservationTimeServiceRequest(
        ReservationDate date,
        ThemeId themeId
) {

    public AvailableReservationTimeServiceRequest {
        validate(date, themeId);
    }

    private void validate(final ReservationDate date, final ThemeId themeId) {
        Validator.of(AvailableReservationTimeServiceRequest.class)
                .notNullField(Fields.date, date, ReservationDate.domainName)
                .notNullField(Fields.themeId, themeId, ThemeId.domainName);
    }
}
