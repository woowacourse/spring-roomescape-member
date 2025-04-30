package roomescape.model;

public class Reservation {
    private final Long id;
    private final UserName userName;
    private final ReservationDateTime reservationDateTime;
    private final Theme theme;

    public Reservation(Long id, UserName userName, ReservationDateTime reservationDateTime, Theme theme) {
        this.id = id;
        this.userName = userName;
        this.reservationDateTime = reservationDateTime;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public UserName getUserName() {
        return userName;
    }

    public ReservationDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
