package roomescape.reservationTime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record ReservationTimeRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public ReservationTimeRequest {
        // TODO : "" -> null로 바뀌어서 처리됨
        if(startAt == null) {
            throw new IllegalArgumentException("[ERROR] 시간을 입력해주세요.");
        }
    }
}
