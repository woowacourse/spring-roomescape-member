package roomescape.reservation.ui.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
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
                .validateNotBlank(Fields.name, name, ReserverName.domainName)
                .validateNotNull(Fields.date, date, ReservationDate.domainName)
                .validateNotNull(Fields.timeId, timeId, ReservationTimeId.domainName)
                .validateNotNull(Fields.themeId, themeId, ThemeId.domainName);
    }
}
