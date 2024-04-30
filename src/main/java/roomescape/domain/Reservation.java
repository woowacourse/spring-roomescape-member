package roomescape.domain;

public record Reservation(Long id, String name, String date, ReservationTime time) {
    public Reservation changeId(Long id) {
        return new Reservation(id, this.name, this.date, this.time);
    }
}
