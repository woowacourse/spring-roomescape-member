package roomescape.theme.repository.entity;

public class ThemeEntity {

    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final boolean isDeleted;

    public ThemeEntity(Long id, String name, String description, String imageUrl, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
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

    public boolean isDeleted() {
        return isDeleted;
    }
}
