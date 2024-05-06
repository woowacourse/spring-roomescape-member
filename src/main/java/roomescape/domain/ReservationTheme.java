package roomescape.domain;

public class ReservationTheme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public ReservationTheme(Long id, String name, String description, String thumbnail) {
        validateId(id);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("invalid id");
        }
    }

    public Long getId() {
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
