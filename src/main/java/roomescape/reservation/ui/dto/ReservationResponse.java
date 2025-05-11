package roomescape.reservation.ui.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldNameConstants;
import roomescape.common.domain.DomainTerm;
import roomescape.common.validate.Validator;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.ui.dto.ThemeResponse;
import roomescape.time.ui.dto.ReservationTimeResponse;

import java.time.LocalDate;
import java.util.List;

@FieldNameConstants(level = AccessLevel.PRIVATE)
public record ReservationResponse(Long reservationId,
                                  Long userId,
                                  LocalDate date,
                                  ReservationTimeResponse time,
                                  ThemeResponse theme) {

    public ReservationResponse {
        validate(reservationId, userId, date, time, theme);
    }

    public static ReservationResponse from(final Reservation domain) {
        return new ReservationResponse(
                domain.getId().getValue(),
                domain.getUserId().getValue(),
                domain.getDate().getValue(),
                ReservationTimeResponse.from(domain.getTime()),
                ThemeResponse.from(domain.getTheme()));
    }

    public static List<ReservationResponse> from(final List<Reservation> domains) {
        return domains.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private void validate(final Long reservationId,
                          final Long userId,
                          final LocalDate date,
                          final ReservationTimeResponse time,
                          final ThemeResponse theme) {
        Validator.of(ReservationResponse.class)
                .validateNotNull(Fields.reservationId, reservationId, DomainTerm.RESERVATION_ID.label())
                .validateNotNull(Fields.userId, userId, DomainTerm.USER_ID.label())
                .validateNotNull(Fields.date, date, DomainTerm.RESERVATION_DATE.label())
                .validateNotNull(Fields.time, time, DomainTerm.RESERVATION_TIME.label())
                .validateNotNull(Fields.theme, theme, DomainTerm.THEME_ID.label());
    }
}
