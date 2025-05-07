package roomescape.reservation.controller.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.utils.Validator;
import roomescape.theme.controller.dto.ThemeWebResponse;
import roomescape.time.controller.dto.ReservationTimeWebResponse;

import java.time.LocalDate;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record ReservationWebResponse(Long id,
                                     String name,
                                     LocalDate date,
                                     ReservationTimeWebResponse time,
                                     ThemeWebResponse theme) {

    public ReservationWebResponse {
        validate(id, name, date, time, theme);
    }

    private void validate(final Long id,
                          final String name,
                          final LocalDate date,
                          final ReservationTimeWebResponse time,
                          final ThemeWebResponse theme) {
        Validator.of(ReservationWebResponse.class)
                .notNullField(Fields.id, id)
                .notBlankField(Fields.name, name)
                .notNullField(Fields.date, date)
                .notNullField(Fields.time, time)
                .notNullField(Fields.theme, theme);
    }
}
