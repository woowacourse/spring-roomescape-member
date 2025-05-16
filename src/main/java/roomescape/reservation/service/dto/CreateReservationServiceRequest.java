package roomescape.reservation.service.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.utils.Validator;

import java.time.LocalDate;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record CreateReservationServiceRequest(
        Long memberId,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public CreateReservationServiceRequest {
        validate(memberId, date, timeId, themeId);
    }

    private void validate(final Long memberId, final LocalDate date, final Long timeId, final Long themeId) {
        Validator.of(CreateReservationServiceRequest.class)
                .notNullField(Fields.memberId, memberId)
                .notNullField(Fields.date, date)
                .notNullField(Fields.timeId, timeId)
                .notNullField(Fields.themeId, themeId);
    }
}
