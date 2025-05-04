package roomescape.reservation.ui.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationId;
import roomescape.reservation.domain.ReserverName;
import roomescape.theme.domain.Theme;
import roomescape.theme.ui.dto.ThemeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.ui.dto.ReservationTimeResponse;

import java.time.LocalDate;
import java.util.List;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record ReservationResponse(Long id,
                                  String name,
                                  LocalDate date,
                                  ReservationTimeResponse time,
                                  ThemeResponse theme) {

    public ReservationResponse {
        validate(id, name, date, time, theme);
    }

    public static ReservationResponse from(final Reservation domain) {
        return new ReservationResponse(
                domain.getId().getValue(),
                domain.getName().getValue(),
                domain.getDate().getValue(),
                ReservationTimeResponse.from(domain.getTime()),
                ThemeResponse.from(domain.getTheme()));
    }

    public static List<ReservationResponse> from(final List<Reservation> domains) {
        return domains.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private void validate(final Long id,
                          final String name,
                          final LocalDate date,
                          final ReservationTimeResponse time,
                          final ThemeResponse theme) {
        Validator.of(ReservationResponse.class)
                .notNullField(Fields.id, id, ReservationId.domainName)
                .notBlankField(Fields.name, name, ReserverName.domainName)
                .notNullField(Fields.date, date, ReservationDate.domainName)
                .notNullField(Fields.time, time, ReservationTime.domainName)
                .notNullField(Fields.theme, theme, Theme.domainName);
    }
}
