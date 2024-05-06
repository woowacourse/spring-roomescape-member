package roomescape.dto.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationAddRequest(Name name, LocalDate date, Long timeId, Long themeId) {

    public String getStringDate() {
        return date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
