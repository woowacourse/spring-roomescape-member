package roomescape.model;

public class Theme {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validateRequiredFields(id, name, description, thumbnail);

        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(String name, String description, String thumbnail) {
        validateRequiredFields(name, description, thumbnail);

        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateRequiredFields(Long id, String name, String description, String thumbnail) {
        if (id == null) {
            throw new IllegalArgumentException("id는 null 일 수 없습니다.");
        }

        validateRequiredFields(name, description, thumbnail);
    }

    private void validateRequiredFields(String name, String description, String thumbnail) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("테마명은 null 이거나 빈 값일 수 없습니다.");
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("설명은 null 이거나 빈 값일 수 없습니다.");
        }

        if (thumbnail == null || thumbnail.isEmpty()) {
            throw new IllegalArgumentException("설명은 null 이거나 빈 값일 수 없습니다.");
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
