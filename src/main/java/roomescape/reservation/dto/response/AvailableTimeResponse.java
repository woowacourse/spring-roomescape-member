package roomescape.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record AvailableTimeResponse(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
                                    LocalTime time, Long timeId, boolean isAvailable) {

}
