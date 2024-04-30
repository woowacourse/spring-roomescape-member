package roomescape.core.domain;

public class ReservationTime {
    private Long id;
    private String startAt;

    public ReservationTime() {
    }

    public ReservationTime(final Long id, final String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(final String startAt) {
        this(null, startAt);
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
