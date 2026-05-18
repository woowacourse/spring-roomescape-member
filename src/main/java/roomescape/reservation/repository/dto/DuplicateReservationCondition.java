package roomescape.reservation.repository.dto;

import java.time.LocalDate;

public record DuplicateReservationCondition (
        LocalDate date,
        Long timeId,
        Long themeId
){
}
