package roomescape.domain.time.entity;

import java.time.LocalTime;
import roomescape.domain.time.dto.response.TimeResponseDTO;

public class Time {

    private final Long id;
    private final LocalTime startAt;

    private Time(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }
    
    public LocalTime getStartAt() {
        return startAt;
    }

    public TimeResponseDTO toResponseDTO() {
        return new TimeResponseDTO(id, startAt);
    }

    public static Time create(LocalTime startAt) {
        return new Time(null, startAt);
    }

    public static Time reconstruct(Long id, LocalTime startAt) {
        return new Time(id, startAt);
    }
}
