package roomescape.reservation.dto.response;

import roomescape.reservation.repository.dto.ReservationDetailFind;
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
