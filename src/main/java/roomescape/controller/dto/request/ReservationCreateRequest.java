package roomescape.controller.dto.request;

import roomescape.service.dto.request.ReservationCreateData;

import java.time.LocalDate;

public record ReservationCreateRequest(
        String name,
        LocalDate date,
        Long timeId
) {
    public ReservationCreateData toData() {
        return new ReservationCreateData(name, date, timeId);
    }
}
