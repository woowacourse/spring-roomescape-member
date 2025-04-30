package roomescape.reservation.ui.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.theme.ui.dto.ThemeResponse;
import roomescape.time.ui.dto.ReservationTimeResponse;

import java.time.LocalDate;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record ReservationResponse(Long id,
                                  String name,
                                  LocalDate date,
                                  ReservationTimeResponse time,
                                  ThemeResponse theme) {

    public ReservationResponse {
        validate(id, name, date, time, theme);
    }

    private void validate(final Long id,
                          final String name,
                          final LocalDate date,
                          final ReservationTimeResponse time,
                          final ThemeResponse theme) {
        Validator.of(ReservationResponse.class)
                .notNullField(Fields.id, id)
                .notBlankField(Fields.name, name)
                .notNullField(Fields.date, date)
                .notNullField(Fields.time, time)
                .notNullField(Fields.theme, theme);
    }
}
