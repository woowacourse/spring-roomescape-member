package roomescape.global.time;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class SystemTimeManager implements TimeManager {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    @Override
    public LocalDateTime nowDateTime() {
        return LocalDateTime.now(KOREA_ZONE);
    }
}
