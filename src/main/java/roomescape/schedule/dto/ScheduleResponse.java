package roomescape.schedule.dto;

import roomescape.schedule.model.Schedule;

import java.time.LocalDateTime;

public class ScheduleResponse {

    private final Long id;
    private final String themeName;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;


    private ScheduleResponse(Long id, String themeName, LocalDateTime startAt, LocalDateTime endAt) {
        this.id = id;
        this.themeName = themeName;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static ScheduleResponse of(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getTheme().getName(),
                schedule.getStartAt(),
                schedule.getEndAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getThemeName() {
        return themeName;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }
}
