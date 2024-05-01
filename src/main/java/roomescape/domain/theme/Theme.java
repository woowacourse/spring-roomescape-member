package roomescape.domain.theme;

public record Theme(Long id, String name, String description, String thumbnail) {
    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }
}
