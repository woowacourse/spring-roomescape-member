package roomescape.reservation.ui.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;

import java.time.LocalDate;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record CreateReservationWebRequest(String name,
                                          LocalDate date,
                                          Long timeId) {

    public CreateReservationWebRequest {
        validate(name, date, timeId);
    }

    private void validate(final String name, final LocalDate date, final Long timeId) {
        Validator.of(CreateReservationWebRequest.class)
                .notBlankField(Fields.name, name)
                .notNullField(Fields.date, date)
                .notNullField(Fields.timeId, timeId);
    }
}
