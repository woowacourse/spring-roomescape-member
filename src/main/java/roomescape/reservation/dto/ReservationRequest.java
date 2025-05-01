package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.globalException.RequestInvalidException;

public record ReservationRequest(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
    public ReservationRequest{
        if(name == null || date == null || timeId == null || themeId == null){
            throw new RequestInvalidException();
        }
    }
}
