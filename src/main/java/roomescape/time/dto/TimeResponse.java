package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record TimeResponse(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {
}
