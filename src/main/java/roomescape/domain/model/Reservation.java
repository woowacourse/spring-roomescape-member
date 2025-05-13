package roomescape.domain.model;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final Long memberId;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, Long memberId, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.memberId = memberId;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Long memberId, LocalDate date, ReservationTime time, Theme theme) {
        this(null, memberId, date, time, theme);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        if (this.id == null || that.id == null) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
