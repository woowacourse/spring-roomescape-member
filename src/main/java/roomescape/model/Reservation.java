package roomescape.model;

import java.time.LocalDate;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 20;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        validateName(name);
    }

    private void validateName(String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new RoomescapeException(ErrorCode.RESERVATION_WRONG_NAME);
        }
    }
}
