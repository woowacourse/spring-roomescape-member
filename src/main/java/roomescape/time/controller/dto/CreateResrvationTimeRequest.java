package roomescape.time.controller.dto;

import java.time.LocalTime;

public class CreateResrvationTimeRequest {

    private final LocalTime startAt;

    public CreateResrvationTimeRequest(LocalTime startAt) {
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
