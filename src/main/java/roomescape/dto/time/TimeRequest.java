package roomescape.dto.time;

import java.time.LocalTime;

public record TimeRequest(LocalTime startAt) {

    public TimeRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("시간이 입력되지 않았습니다.");
        }
    }
}
