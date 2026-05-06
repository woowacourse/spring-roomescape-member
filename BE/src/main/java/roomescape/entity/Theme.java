package roomescape.entity;

public record Theme(
        Long id,
        String name,
        String description,
        String thumbnailUrl
) {
    public static Theme createWithNullId(String name, String description, String thumbnailUrl) {
        return new Theme(null, name, description, thumbnailUrl);
    }

    public static Theme createWithId(Long id, String name, String description, String thumbnailUrl) {
        return new Theme(id, name, description, thumbnailUrl);
    }

    public Theme appendId(Long id) {
        return new Theme(id, name, description, thumbnailUrl);
    }
}
