package roomescape.service.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ReservationOptionResponse(
        List<LocalDate> dates,
        List<ThemeResponse> themes
) {
}
