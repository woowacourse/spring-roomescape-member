package roomescape.service.dto;

import java.time.DateTimeException;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class ReservationRequest {
    private final LocalDate date;
    private final String name;
    private final Long timeId;
    private final Long themeId;

    public ReservationRequest(String date, String name, String timeId, String themeId) {
        validate(date, name, timeId, themeId);
        this.date = LocalDate.parse(date);
        this.name = name;
        this.timeId = Long.parseLong(timeId);
        this.themeId = Long.parseLong(themeId);
    }

    public void validate(String date, String name, String timeId, String themeId) {
        if (date == null || name.isBlank() || timeId == null || themeId == null) {
            throw new IllegalArgumentException();
        }
        try {
            LocalDate.parse(date);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException();
        }
    }

    public Reservation toReservation(ReservationTime reservationTime, Theme theme) {
        return new Reservation(name, date, reservationTime, theme);
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
