package roomescape.time.ui.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.utils.Validator;

import java.time.LocalTime;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record ReservationTimeResponse(Long id,
                                      LocalTime startAt) {

    public ReservationTimeResponse {
        validate(id, startAt);
    }

    private void validate(final Long id, final LocalTime startAt) {
        Validator.of(ReservationTimeResponse.class)
                .notNullField(ReservationTimeResponse.Fields.id, id)
                .notNullField(ReservationTimeResponse.Fields.startAt, startAt);
    }
}
