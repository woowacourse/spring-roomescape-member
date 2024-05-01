package roomescape.domain;

public class Theme {

    private final String name;
    private final String description;
    private final Thumbnail thumbnail;

    public Theme(String name, String description, Thumbnail thumbnail) {
        validate(name, description);
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme of(String name, String description, String thumbnail) {
        return new Theme(name, description, new Thumbnail(thumbnail));
    }

    private void validate(String name, String description) {
        validateNull(name, description);
    }

    private void validateNull(String name, String description) {
        if (name.isBlank() || description.isBlank()) {
            throw new IllegalArgumentException("이름과 설명은 공백일 수 없습니다");
        }
    }
}
