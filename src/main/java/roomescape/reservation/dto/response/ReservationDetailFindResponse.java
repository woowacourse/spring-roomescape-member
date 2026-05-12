package roomescape.reservation.dto.response;

import roomescape.reservation.repository.projection.ReservationDetailProjection;
import roomescape.reservationtime.dto.response.TimeInformation;
import roomescape.theme.dto.response.ThemeFindResponse;

import java.time.LocalDate;
import java.util.List;

public record ReservationDetailFindResponse(
        Long id,
        String name,
        LocalDate date,
        ThemeFindResponse theme,
        TimeInformation time
) {
    public static List<ReservationDetailFindResponse> from(List<ReservationDetailProjection> rows) {
        return rows.stream()
                .map(row -> new ReservationDetailFindResponse(
                        row.id(),
                        row.name(),
                        row.date(),
                        row.themeFindResponse(),
                        row.timeInformation()
                ))
                .toList();
    }

    public static ReservationDetailFindResponse from(ReservationDetailProjection row) {
        return new ReservationDetailFindResponse(
                row.id(),
                row.name(),
                row.date(),
                row.themeFindResponse(),
                row.timeInformation()
        );
    }
}
