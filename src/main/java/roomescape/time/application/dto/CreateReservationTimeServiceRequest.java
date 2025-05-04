package roomescape.time.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.time.domain.ReservationTime;

import java.time.LocalTime;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record CreateReservationTimeServiceRequest(LocalTime startAt) {

    public CreateReservationTimeServiceRequest {
        validate(startAt);
    }

    public ReservationTime toDomain() {
        return ReservationTime.withoutId(
                startAt);
    }

    private void validate(final LocalTime startAt) {
        Validator.of(CreateReservationTimeServiceRequest.class)
                .notNullField(Fields.startAt, startAt, ReservationTime.domainName);
    }
}
