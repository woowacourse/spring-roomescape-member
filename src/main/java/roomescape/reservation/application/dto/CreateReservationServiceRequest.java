package roomescape.reservation.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;

import java.time.LocalDate;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record CreateReservationServiceRequest(String name,
                                              LocalDate date,
                                              Long timeId) {

    public CreateReservationServiceRequest {
        validate(name, date, timeId);
    }

    private void validate(final String name, final LocalDate date, final Long time) {
        Validator.of(CreateReservationServiceRequest.class)
                .notBlankField(CreateReservationServiceRequest.Fields.name, name)
                .notNullField(CreateReservationServiceRequest.Fields.date, date)
                .notNullField(CreateReservationServiceRequest.Fields.timeId, time);
    }
}
