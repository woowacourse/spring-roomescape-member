package roomescape.dto.reservation;

import java.time.LocalDate;

public record ReservationInfo(String name, LocalDate date, long timeId, long themeId) {
    public ReservationInfo(String name, ReservationRequest request) {
        this(name, request.date(), request.timeId(), request.themeId());
    }
}
