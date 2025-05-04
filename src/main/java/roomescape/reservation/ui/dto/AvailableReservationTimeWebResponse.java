package roomescape.reservation.ui.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.reservation.application.dto.AvailableReservationTimeServiceResponse;
import roomescape.reservation.domain.BookedStatus;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;

import java.time.LocalTime;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record AvailableReservationTimeWebResponse(
        LocalTime startAt,
        Long timeId,
        Boolean isBooked
) {

    public AvailableReservationTimeWebResponse {
        validate(startAt, timeId, isBooked);
    }

    public static AvailableReservationTimeWebResponse from(
            final AvailableReservationTimeServiceResponse serviceResponse) {
        return new AvailableReservationTimeWebResponse(
                serviceResponse.time().getStartAt(),
                serviceResponse.time().getId().getValue(),
                serviceResponse.isBooked().isBooked());
    }

    private void validate(final LocalTime startAt, final Long timeId, final Boolean isBooked) {
        Validator.of(AvailableReservationTimeWebResponse.class)
                .notNullField(Fields.startAt, startAt, ReservationTime.domainName)
                .notNullField(Fields.timeId, timeId, ReservationTimeId.domainName)
                .notNullField(Fields.isBooked, isBooked, BookedStatus.domainName);
    }
}
