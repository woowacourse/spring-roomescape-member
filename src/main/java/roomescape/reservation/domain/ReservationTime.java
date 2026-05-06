package roomescape.reservation.domain;

public class ReservationTime {
    private final Long id;
    private final String startAt;
    private final String endAt;

    public ReservationTime(String startAt, String endAt) {
        this(null, startAt, endAt);
    }

    public ReservationTime(Long id, String startAt, String endAt) {
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public ReservationTime withId(Long id) {
        return new ReservationTime(id, startAt, endAt);
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public String getEndAt() {
        return endAt;
    }
}
