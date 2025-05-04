package roomescape.reservation.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.reservation.domain.BookedStatus;
import roomescape.time.domain.ReservationTime;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record AvailableReservationTimeServiceResponse(
        ReservationTime time,
        BookedStatus isBooked
) {

    public AvailableReservationTimeServiceResponse {
        validate(time, isBooked);
    }

    private void validate(final ReservationTime time, final BookedStatus isBooked) {
        Validator.of(AvailableReservationTimeServiceResponse.class)
                .notNullField(Fields.time, time, ReservationTime.domainName)
                .notNullField(Fields.isBooked, isBooked, BookedStatus.domainName);
    }
}
