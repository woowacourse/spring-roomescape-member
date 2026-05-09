package roomescape.domain;

public record Theme(
        Long id,
        String name,
        String description,
        String thumbnailUrl
) {
    public static Theme create(String name, String description, String thumbnailUrl) {
        return new Theme(null, name, description, thumbnailUrl);
    }

    public static Theme createRow(Long id, String name, String description, String thumbnailUrl) {
        return new Theme(id, name, description, thumbnailUrl);
    }

    public Theme appendId(Long id) {
        return new Theme(id, name, description, thumbnailUrl);
    }
}
