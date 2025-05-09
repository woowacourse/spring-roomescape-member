package roomescape.domain;

public class ReservationName {

    private final Long id;
    private final String name;

    public ReservationName(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
