package roomescape.model;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validateNotNull(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private static void validateNotNull(final String name, final String description, final String thumbnail) {
        if (name == null || description == null || thumbnail == null) {
            throw new IllegalArgumentException("테마의 이름, 설명, 썸네일은 null이 될 수 없습니다.");
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
