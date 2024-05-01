package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class ReservationAddRequest {
    private LocalDate date;
    private String name;
    private Long timeId;
    private Long themeId;

    public ReservationAddRequest() {

    }

    public ReservationAddRequest(LocalDate date, String name, Long timeId, Long themeId) {
        this.date = date;
        this.name = name;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public Reservation toEntity(ReservationTime reservationTime, Theme theme) {
        return new Reservation(null, name, date, reservationTime, theme);
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }
}
