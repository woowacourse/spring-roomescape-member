package roomescape.reservationtime.domain;


import lombok.Getter;

@Getter
public class ReservationTimeAvailability {
    private final ReservationTime reservationTime;
    private final boolean isAvailable;


    public ReservationTimeAvailability(ReservationTime reservationTime, boolean isAvailable) {
        this.reservationTime = reservationTime;
        this.isAvailable = isAvailable;
    }

}
