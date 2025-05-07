package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import roomescape.model.MemberName;
import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

public record ReservationRequestDto(@JsonProperty(value = "name", defaultValue = "name") String name,
                                    @JsonProperty("date") LocalDate date,
                                    @JsonProperty("timeId") Long timeId,
                                    @JsonProperty("themeId") Long themeId
) {

    public Reservation toEntity(Long id, ReservationTime reservationTime, Theme theme) {
        return new Reservation(id,
                new MemberName(name()),
                new ReservationDateTime(date(), reservationTime), theme);
    }

    public ReservationRequestDto(String name, LocalDate date, Long timeId, Long themeId) {
        if (name == null || date == null || timeId == null || themeId == null) {
            throw new IllegalArgumentException("이름,날짜,시간id,테마id는 필수입니다");
        }
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }
}
