package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import roomescape.reservation.service.dto.CreateReservationServiceRequest;

public record CreateAdminReservationRequest(LocalDate date, long timeId, long themeId, long memberId) {

    public CreateReservationServiceRequest toCreateReservationServiceRequest() {
        return new CreateReservationServiceRequest(date, timeId, themeId, memberId);
    }
}

