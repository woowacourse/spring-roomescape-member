package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.exception.RoomescapeErrorCode;
import roomescape.exception.RoomescapeException;

public class Reservation {
    private final Long id;
    private final PlayerName name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(PlayerName name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, PlayerName name, LocalDate date, ReservationTime time, Theme theme) {
        if (date == null) {
            throw new RoomescapeException(RoomescapeErrorCode.BAD_REQUEST, "예약 날짜는 필수입니다.");
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Reservation withId(long id) {
        return new Reservation(id, name, date, time, theme);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
