package roomescape.reservation.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReserverName;
import roomescape.reservation.ui.dto.CreateReservationWebRequest;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record CreateReservationServiceRequest(String name,
                                              LocalDate date,
                                              Long timeId,
                                              Long themeId) {

    public CreateReservationServiceRequest {
        validate(name, date, timeId, themeId);
    }

    public static CreateReservationServiceRequest from(final CreateReservationWebRequest webRequest) {
        return new CreateReservationServiceRequest(
                webRequest.name(),
                webRequest.date(),
                webRequest.timeId(),
                webRequest.themeId());
    }

    public Reservation toDomain(final ReservationTime time, final Theme theme) {
        return Reservation.withoutId(
                ReserverName.from(name),
                ReservationDate.from(date),
                time,
                theme);
    }

    private void validate(final String name, final LocalDate date, final Long timeId, final Long themeId) {
        Validator.of(CreateReservationServiceRequest.class)
                .notBlankField(Fields.name, name)
                .notNullField(Fields.date, date)
                .notNullField(Fields.timeId, timeId)
                .notNullField(Fields.themeId, themeId);
    }
}
