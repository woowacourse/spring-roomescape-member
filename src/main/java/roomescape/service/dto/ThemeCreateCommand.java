package roomescape.service.dto;

public class ThemeCreateCommand {
    private final String name;
    private final String description;
    private final String thumbnailUrl;

    public ThemeCreateCommand(String name, String description, String thumbnailUrl) {
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

    public String getThumbnail() {
        return thumbnailUrl;
    }
}
