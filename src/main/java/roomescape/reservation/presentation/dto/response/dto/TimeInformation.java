package roomescape.reservation.presentation.dto.response.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record TimeInformation(@NotNull Long id,
                              @JsonFormat(pattern = "HH:mm") @NotNull LocalTime time) {
}
