package roomescape.time.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record TimeRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public TimeRequest {
        if(startAt == null) {
            throw new IllegalArgumentException("[ERROR] 시간을 입력해주세요.");
        }
    }
}
