package roomescape.reservation.ui.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.time.ui.dto.ReservationTimeResponse;

import java.time.LocalDate;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record ReservationResponse(Long id,
                                  String name,
                                  LocalDate date,
                                  ReservationTimeResponse time) {

    public ReservationResponse {
        validate(id, name, date, time);
    }

    private void validate(final Long id,
                          final String name,
                          final LocalDate date,
                          final ReservationTimeResponse time) {
        Validator.of(ReservationResponse.class)
                .notNullField(ReservationResponse.Fields.id, id)
                .notBlankField(ReservationResponse.Fields.name, name)
                .notNullField(ReservationResponse.Fields.date, date)
                .notNullField(ReservationResponse.Fields.time, time);
    }
}
