package roomescape.service.dto.response;

import java.time.LocalDate;
import java.util.List;

public record AvailableDateResponse(
        List<LocalDate> dates
) {
}
