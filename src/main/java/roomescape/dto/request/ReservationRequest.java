package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

public record ReservationRequest(@JsonProperty("memberId") Long memberId,
                                 @JsonProperty("date") LocalDate date,
                                 @JsonProperty("timeId") Long timeId,
                                 @JsonProperty("themeId") Long themeId
) {

    public Reservation toEntity(Long id, Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                id,
                member,
                new ReservationDateTime(date(), reservationTime),
                theme
        );
    }

    public ReservationRequest(Long memberId, LocalDate date, Long timeId, Long themeId) {
        if (memberId == null || date == null || timeId == null || themeId == null) {
            throw new IllegalArgumentException("멤버 id,날짜, 시간id, 테마id는 필수입니다");
        }
        this.memberId = memberId;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }
}
