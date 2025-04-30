package roomescape.domain.repository.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record AvailableTimesData(
        
        Long id,

        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,

        boolean alreadyBooked
) {
}
