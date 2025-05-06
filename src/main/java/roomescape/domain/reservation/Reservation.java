package roomescape.domain.reservation;

import java.time.LocalDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.roomtheme.RoomTheme;
import roomescape.exception.custom.InvalidInputException;

public class Reservation {

    private static final int NON_SAVED_STATUS = 0;
    private static final int MAX_LENGTH = 255;

    private final long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final RoomTheme theme;

    public Reservation(final long id,
                       final String name,
                       final LocalDate date,
                       final ReservationTime time,
                       final RoomTheme theme) {
        validateInvalidInput(name, date, time, theme);

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final String name,
                       final LocalDate date,
                       final ReservationTime reservationTime,
                       final RoomTheme theme) {
        this(NON_SAVED_STATUS, name, date, reservationTime, theme);
    }

    public boolean validatePastDateAndTime() {
        final LocalDate currentDate = LocalDate.now();

        final boolean isPastDate = date.isBefore(currentDate);
        final boolean isPastTime = date.isEqual(currentDate) && time.validatePastTime();

        return isPastDate || isPastTime;
    }

    private void validateInvalidInput(final String name, final LocalDate date,
                                      final ReservationTime reservationTime, final RoomTheme theme) {
        validateNotNull(name, date, reservationTime, theme);
        validateValidLength(name);
    }

    private void validateNotNull(final String name, final LocalDate date,
                                 final ReservationTime reservationTime, final RoomTheme theme) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("예약자 명은 빈 값이 입력될 수 없습니다");
        }
        if (date == null) {
            throw new InvalidInputException("예약 날짜는 빈 값이 입력될 수 없습니다");
        }
        if (reservationTime == null) {
            throw new InvalidInputException("예약 시간은 빈 값이 입력될 수 없습니다");
        }
        if (theme == null) {
            throw new InvalidInputException("예약 테마는 빈 값이 입력될 수 없습니다");
        }
    }

    private void validateValidLength(final String name) {
        if (name.length() > MAX_LENGTH) {
            throw new InvalidInputException("예약자 명은 255자를 초과할 수 없습니다.");
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public RoomTheme getTheme() {
        return theme;
    }
}
