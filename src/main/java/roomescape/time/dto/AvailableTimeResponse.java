package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.time.domain.Time;

public record AvailableTimeResponse(Long timeId,
                                    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
                                    Boolean alreadyBooked) {
}
