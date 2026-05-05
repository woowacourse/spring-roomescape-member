package roomescape.schedule.model;

import roomescape.theme.model.Theme;

import java.time.LocalDateTime;

public class Schedule {

    private Long id;
    private Theme theme;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public Schedule() {
    }

    public Schedule(LocalDateTime startAt, LocalDateTime endAt, Theme theme) {
        this(null, startAt, endAt, theme);
    }

    public Schedule(Long id, LocalDateTime startAt, LocalDateTime endAt, Theme theme) {
        validateStartAt(startAt);
        validateEndAt(endAt);
        validateTheme(theme);
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
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
            throw new IllegalArgumentException("예약 시작 시간은 필수입니다.");
        }
    }

    private void validateEndAt(LocalDateTime endAt) {
        if (endAt == null) {
            throw new IllegalArgumentException("예약 시작 시간은 필수입니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("테마 정보는 필수입니다.");
        }
    }
}
