package roomescape.reservation.controller.dto;

import java.time.LocalDate;

public record UpdateReservationRequest (
        LocalDate date,
        Long timeId
){
}
