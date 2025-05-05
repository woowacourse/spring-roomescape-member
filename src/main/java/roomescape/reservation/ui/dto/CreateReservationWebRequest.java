package roomescape.reservation.ui.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.domain.DomainTerm;
import roomescape.common.validate.Validator;
import roomescape.reservation.application.dto.CreateReservationServiceRequest;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReserverName;
import roomescape.theme.domain.ThemeId;
import roomescape.time.domain.ReservationTimeId;

import java.time.LocalDate;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record CreateReservationWebRequest(String name,
                                          LocalDate date,
                                          Long timeId,
                                          Long themeId) {

    public CreateReservationWebRequest {
        validate(name, date, timeId, themeId);
    }

    public CreateReservationServiceRequest toServiceRequest() {
        return new CreateReservationServiceRequest(
                ReserverName.from(name),
                ReservationDate.from(date),
                ReservationTimeId.from(timeId),
                ThemeId.from(themeId));
    }

    private void validate(final String name, final LocalDate date, final Long timeId, final Long themeId) {
        Validator.of(CreateReservationWebRequest.class)
                .validateNotBlank(Fields.name, name, DomainTerm.RESERVER_NAME.label())
                .validateNotNull(Fields.date, date, DomainTerm.RESERVATION_DATE.label())
                .validateNotNull(Fields.timeId, timeId, DomainTerm.RESERVATION_TIME_ID.label())
                .validateNotNull(Fields.themeId, themeId, DomainTerm.THEME_ID.label());
    }
}
