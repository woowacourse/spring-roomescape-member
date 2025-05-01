package roomescape.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.List;
import roomescape.domain.repository.dto.TimeDataWithBookingInfo;

public record TimeResponseWithBookingInfo(

        Long id,

        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,

        boolean alreadyBooked
) {
    public static TimeResponseWithBookingInfo from(TimeDataWithBookingInfo dto) {
        return new TimeResponseWithBookingInfo(dto.id(), dto.startAt(), dto.alreadyBooked());
    }

    public static List<TimeResponseWithBookingInfo> from(List<TimeDataWithBookingInfo> dtos) {
        return dtos.stream()
                .map(TimeResponseWithBookingInfo::from)
                .toList();
    }
}
