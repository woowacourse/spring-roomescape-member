package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record UserReservationRequest(@JsonProperty("date") LocalDate date,
                                     @JsonProperty("timeId") Long timeId,
                                     @JsonProperty("themeId") Long themeId
) {


    public UserReservationRequest(LocalDate date, Long timeId, Long themeId) {
        if (date == null || timeId == null || themeId == null) {
            throw new IllegalArgumentException("멤버 id,날짜, 시간id, 테마id는 필수입니다");
        }

        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }
}
