package roomescape.reservation.presentation.dto.response;

import roomescape.reservation.infra.dto.ReservationDetailFind;

import java.time.LocalDate;
import java.util.List;

public record ReservationDetailFindResponse(
        Long id,
        String name,
        LocalDate date,
        ThemeFindResponse theme,
        TimeInformation time
) {
    public static List<ReservationDetailFindResponse> from(List<ReservationDetailFind> reservationRows) {
        return reservationRows.stream()
                .map(row -> new ReservationDetailFindResponse(
                        row.id(),
                        row.name(),
                        row.date(),
                        row.themeFindResponse(),
                        row.timeInformation()
                ))
                .toList();
    }
}
