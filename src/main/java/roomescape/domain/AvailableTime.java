package roomescape.domain;

import java.time.LocalTime;

public class AvailableTime {
    private final LocalTime time;
    private final boolean available;

    public AvailableTime(LocalTime time, boolean available) {
        this.time = time;
        this.available = available;
    }

    public LocalTime getTime() {
        return time;
    }

    public boolean getAvailable() {
        return available;
    }
}
