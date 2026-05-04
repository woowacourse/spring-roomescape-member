package roomescape.model;

public class Theme {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;

    public Theme() {
    }

    public Theme(String name, String description, String imageUrl) {
        this(null, name, description, imageUrl);
    }

    public Theme(Long id, String name, String description, String imageUrl) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 필수이며, 공백일 수 없습니다.");
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

    public String getImageUrl() {
        return imageUrl;
    }
}
