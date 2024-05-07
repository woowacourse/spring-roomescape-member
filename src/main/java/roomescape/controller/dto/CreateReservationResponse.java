package roomescape.controller.dto;

import java.time.LocalDate;

public record CreateReservationResponse(Long id,
                                        String name,
                                        LocalDate date,
                                        CreateTimeResponse time,
                                        CreateThemeResponse theme) {

}
