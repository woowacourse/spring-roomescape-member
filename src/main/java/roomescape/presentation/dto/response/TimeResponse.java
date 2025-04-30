package roomescape.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.List;
import roomescape.application.dto.TimeDto;

public record TimeResponse(

        long id,

        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {
    public static TimeResponse from(TimeDto timeDto) {
        return new TimeResponse(timeDto.id(), timeDto.startAt());
    }

    public static List<TimeResponse> from(List<TimeDto> timeDtos) {
        return timeDtos.stream()
                .map(TimeResponse::from)
                .toList();
    }
}
