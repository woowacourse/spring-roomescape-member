package roomescape.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ReservationCreateRequestDto(String name, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                          Long timeId, Long themeId) {
//    public Reservation createWithoutId(ReservationTime reservationTime, Theme theme) {
//        return new Reservation(null, name, date, reservationTime, theme);
//    }
}
