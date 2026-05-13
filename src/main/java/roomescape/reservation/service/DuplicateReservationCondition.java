package roomescape.reservation.service;

import java.time.LocalDate;

public record DuplicateReservationCondition (
        LocalDate date,
        Long timeId,
        Long themeId
){
}
