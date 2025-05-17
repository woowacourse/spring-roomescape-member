package roomescape.business.domain.reservation;

public final class ReservationTheme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public ReservationTheme(Long id, String name, String description, String thumbnail) {
        if (name == null || description == null || thumbnail == null) {
            throw new IllegalArgumentException("테마 정보는 null일 수 없습니다.");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public ReservationTheme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
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
