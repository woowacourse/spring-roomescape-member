package roomescape.domain;

public record Theme(Long id, String name, String description, String thumbnailUrl) {

    public static Theme transientOf(String name, String description, String thumbnailUrl) {
        return new Theme(
                null,
                name,
                description,
                thumbnailUrl
        );
    }
}
