package roomescape.model;

public class Reservation {
    private final Long id;
    private final UserName userName;
    private final ReservationDateTime reservationDateTime;


    public Reservation(Long id, UserName userName, ReservationDateTime reservationDateTime) {
        this.id = id;
        this.userName = userName;
        this.reservationDateTime = reservationDateTime;
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
}
