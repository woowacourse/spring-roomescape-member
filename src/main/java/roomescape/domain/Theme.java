package roomescape.domain;

import java.time.Duration;
import java.time.LocalTime;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final LocalTime startAt;
    private final LocalTime finishAt;
    private final Duration playTime;

    public Theme(Long id, String name, String description, String imageUrl, LocalTime startAt, LocalTime finishAt, Duration playTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.startAt = startAt;
        this.finishAt = finishAt;
        this.playTime = playTime;
    }

    public static Theme of(Long id, String name, String description, String imageUrl, LocalTime startAt, LocalTime finishAt, Duration playTime) {
        return new Theme(id, name, description, imageUrl, startAt, finishAt, playTime);
    }

    public static Theme of(String name, String description, String imageUrl, LocalTime startAt, LocalTime finishAt, Duration playTime) {
        return new Theme(null, name, description, imageUrl, startAt, finishAt, playTime);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public LocalTime getFinishAt() {
        return finishAt;
    }

    public Duration getPlayTime() {
        return playTime;
    }
}
