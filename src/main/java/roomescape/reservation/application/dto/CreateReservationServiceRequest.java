package roomescape.reservation.application.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReserverName;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeId;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record CreateReservationServiceRequest(ReserverName name,
                                              ReservationDate date,
                                              ReservationTimeId timeId,
                                              ThemeId themeId) {

    public CreateReservationServiceRequest {
        validate(name, date, timeId, themeId);
    }

    public Reservation toDomain(final ReservationTime time, final Theme theme) {
        return Reservation.withoutId(
                name,
                date,
                time,
                theme);
    }

    private void validate(final ReserverName name,
                          final ReservationDate date,
                          final ReservationTimeId timeId,
                          final ThemeId themeId) {
        Validator.of(CreateReservationServiceRequest.class)
                .validateNotNull(Fields.name, name, ReserverName.domainName)
                .validateNotNull(Fields.date, date, ReservationDate.domainName)
                .validateNotNull(Fields.timeId, timeId, ReservationTimeId.domainName)
                .validateNotNull(Fields.themeId, themeId, ThemeId.domainName);
    }
}
