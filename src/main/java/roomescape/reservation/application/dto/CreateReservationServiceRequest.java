package roomescape.reservation.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.reservation_time.domain.ReservationTime;

import java.time.LocalDate;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record CreateReservationServiceRequest(String name,
                                              LocalDate date,
                                              ReservationTime time) {

    public CreateReservationServiceRequest {
        validate(name, date, time);
    }

    private void validate(final String name, final LocalDate date, final ReservationTime time) {
        Validator.of(CreateReservationServiceRequest.class)
                .notBlankField(CreateReservationServiceRequest.Fields.name, name)
                .notNullField(CreateReservationServiceRequest.Fields.date, date)
                .notNullField(CreateReservationServiceRequest.Fields.time, time);
    }
}
