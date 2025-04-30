package roomescape.reservation.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;

import java.time.LocalDate;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record CreateReservationServiceRequest(String name,
                                              LocalDate date,
                                              Long timeId,
                                              Long themeId) {

    public CreateReservationServiceRequest {
        validate(name, date, timeId, themeId);
    }

    private void validate(final String name, final LocalDate date, final Long timeId, final Long themeId) {
        Validator.of(CreateReservationServiceRequest.class)
                .notBlankField(Fields.name, name)
                .notNullField(Fields.date, date)
                .notNullField(Fields.timeId, timeId)
                .notNullField(Fields.themeId, themeId);
    }
}
