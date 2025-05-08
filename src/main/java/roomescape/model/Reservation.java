package roomescape.model;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;
    private final Member member;

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme,
                       Member member) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme, Member member) {
        this(null, name, date, reservationTime, theme, member);
        validateReservationDateInFuture();
    }

    private void validateReservationDateInFuture() {
        if (!this.date.isAfter(LocalDate.now())) {
            throw new IllegalStateException("과거 및 당일 예약은 불가능합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return this.reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
