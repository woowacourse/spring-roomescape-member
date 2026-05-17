package roomescape.support.time;

import java.time.LocalDateTime;
import roomescape.global.time.TimeManager;

public class FixedTimeManager implements TimeManager {

    private LocalDateTime current;

    public FixedTimeManager(final LocalDateTime current) {
        this.current = current;
    }

    @Override
    public LocalDateTime nowDateTime() {
        return current;
    }

}
