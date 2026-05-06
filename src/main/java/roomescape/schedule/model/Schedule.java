package roomescape.schedule.model;

import roomescape.theme.model.Theme;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Schedule {

    private static final LocalTime OPENING_TIME = LocalTime.of(10, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(20, 0);

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

    private LocalDateTime calculateEndAt(LocalDateTime startAt, Theme theme) {
        LocalTime requiredTime = theme.getRequiredTime();

        LocalDateTime endAt = startAt.plusHours(requiredTime.getHour())
                .plusMinutes(requiredTime.getMinute());

        validateEndAt(endAt);
        return endAt;
    }

    private void validateStartAt(LocalDateTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("예약 시작 시간은 필수입니다.");
        }

        if (startAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 날짜/시간에는 스케줄을 생성할 수 없습니다.");
        }

        LocalTime startTime = startAt.toLocalTime();

        if (startTime.isBefore(OPENING_TIME)) {
            throw new IllegalArgumentException("오전 10시 이전에는 예약이 불가능합니다.");
        }
    }

    private void validateEndAt(LocalDateTime endAt) {
        if (endAt == null) {
            throw new IllegalArgumentException("예약 시작 시간은 필수입니다.");
        }

        LocalTime endTime = endAt.toLocalTime();

        if (endTime.isAfter(CLOSE_TIME)) {
            throw new IllegalArgumentException("오후 8시 이후에는 예약이 불가능합니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("테마 정보는 필수입니다.");
        }
    }
}
