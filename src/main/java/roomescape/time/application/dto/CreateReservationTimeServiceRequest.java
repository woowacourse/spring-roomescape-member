package roomescape.time.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;

import java.time.LocalTime;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record CreateReservationTimeServiceRequest(LocalTime startAt) {

    public CreateReservationTimeServiceRequest {
        validate(startAt);
    }

    private void validate(final LocalTime startAt) {
        Validator.of(CreateReservationTimeServiceRequest.class)
                .notNullField(CreateReservationTimeServiceRequest.Fields.startAt, startAt);
    }
}
