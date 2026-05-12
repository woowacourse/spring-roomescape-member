package roomescape.schedule.model;

import roomescape.theme.model.Theme;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Schedule {

    private Long id;
    private Theme theme;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public Schedule() {
    }

    public Schedule(LocalDateTime startAt, Theme theme) {
        this(null, startAt, theme);
    }

    public Schedule(Long id, LocalDateTime startAt, Theme theme) {
        validateStartAt(startAt);
        validateTheme(theme);
        this.id = id;
        this.startAt = startAt;
        this.endAt = calculateEndAt(startAt, theme);
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public Theme getTheme() {
        return theme;
    }

    private void validateStartAt(LocalDateTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("스케줄 시작 시간은 필수입니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("테마 정보는 필수입니다.");
        }
    }

    private LocalDateTime calculateEndAt(LocalDateTime startAt, Theme theme) {
        LocalTime requiredTime = theme.getRequiredTime();
        return startAt.plusHours(requiredTime.getHour())
                .plusMinutes(requiredTime.getMinute());
    }

    public boolean isBefore() {
        return startAt.isBefore(LocalDateTime.now());
    }
}
