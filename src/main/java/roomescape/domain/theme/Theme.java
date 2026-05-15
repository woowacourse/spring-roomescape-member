package roomescape.domain.theme;

public record Theme(Long id, String name, String description, String imageUrl) {

    public Theme(String name, String description, String imageUrl) {
        this(null, name, description, imageUrl);
    }
}
