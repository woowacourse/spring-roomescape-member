package roomescape.controller.dto;

public class ThemeCreateRequest {
    private final String name;
    private final String description;
    private final String thumbnailUrl;

    public ThemeCreateRequest(String name, String description, String thumbnailUrl) {
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
