package roomescape.controller.dto;

import java.time.LocalTime;
import java.util.List;

import roomescape.domain.ThemeSlot;
import roomescape.domain.Time;

public record TimeResponse(long id, LocalTime startAt, boolean isAvailable) {

    public static TimeResponse from(Time time) {
        return new TimeResponse(time.getId(), time.getStartAt(), true);
    }

    public static TimeResponse from(ThemeSlot themeSlot) {
        return new TimeResponse(themeSlot.getTime().getId(), themeSlot.getTime().getStartAt(), !themeSlot.isReserved());
    }

    public static List<TimeResponse> availableOf(List<Time> allTimes, List<Long> reservedTimeId) {
        return allTimes.stream()
                .map(
                        time -> {
                            boolean isAvailable = !reservedTimeId.contains(time.getId());
                            return new TimeResponse(time.getId(), time.getStartAt(), isAvailable);
                        }
                ).toList();
    }
}
