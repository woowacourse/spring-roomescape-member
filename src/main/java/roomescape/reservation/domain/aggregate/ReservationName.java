package roomescape.reservation.domain.aggregate;

public final class ReservationName {
    private final String name;

    public ReservationName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
