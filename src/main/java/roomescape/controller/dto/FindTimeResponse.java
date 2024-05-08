package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record FindTimeResponse(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) { }
