package roomescape.reservation.ui.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.utils.Validator;

import java.time.LocalTime;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record AvailableReservationTimeWebResponse(
        LocalTime startAt,
        Long timeId,
        Boolean isBooked
) {

    public AvailableReservationTimeWebResponse(final LocalTime startAt, final Long timeId, final Boolean isBooked) {
        validate(startAt, timeId, isBooked);
        this.startAt = startAt;
        this.timeId = timeId;
        this.isBooked = isBooked;
    }

    private void validate(final LocalTime startAt, final Long timeId, final Boolean isBooked) {
        Validator.of(AvailableReservationTimeWebResponse.class)
                .notNullField(Fields.startAt, startAt)
                .notNullField(Fields.timeId, timeId)
                .notNullField(Fields.isBooked, isBooked);
    }
}
