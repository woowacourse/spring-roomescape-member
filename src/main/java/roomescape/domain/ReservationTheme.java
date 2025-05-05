package roomescape.domain;

public class ReservationTheme {

    private Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public ReservationTheme(final String name, final String description, final String thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public ReservationTheme assignId(final Long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
