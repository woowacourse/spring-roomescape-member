package roomescape.domain;

public record ReservationTheme(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public ReservationTheme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public ReservationTheme withId(Long id) {
        return new ReservationTheme(id, name, description, thumbnail);
    }
}
