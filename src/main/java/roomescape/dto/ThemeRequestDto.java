package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.model.UserName;

//public record ReservationRequestDto(@JsonProperty(value = "name", defaultValue = "name") String name,
//                                    @JsonProperty("date") LocalDate date,
//                                    @JsonProperty("timeId") Long timeId,
//                                    @JsonProperty("themeId") Long themeId
//) {
//
//    public Reservation toEntity(Long id, ReservationTime reservationTime, Theme theme) {
//        return new Reservation(id,
//                new UserName(name()),
//                new ReservationDateTime(date(), reservationTime), theme);
//    }
//
//    public ReservationRequestDto(String name, java.time.LocalDate date, Long timeId, Long themeId){
//        if (name == null || date == null || timeId == null || themeId == null) throw new IllegalArgumentException();
//        this.name = name;
//        this.date = date;
//        this.timeId = timeId;
//        this.themeId = themeId;
//    }
//}

public record ThemeRequestDto(@JsonProperty("name") String name,
                              @JsonProperty("description") String description,
                              @JsonProperty("thumbnail") String thumbnail
                              ) {
}

