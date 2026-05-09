package roomescape.domain.reservationTime;

public record ReservationTime(long id, String startAt) {
    public static ReservationTime from(long id, ReservationTimeCommand command) {
        return new ReservationTime(id, command.startAt());
    }
}
