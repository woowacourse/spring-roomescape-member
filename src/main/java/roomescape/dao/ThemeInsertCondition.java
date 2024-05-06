package roomescape.dao;

public class ThemeInsertCondition {

    private final String name;
    private final String description;
    private final String thumbnail;

    public ThemeInsertCondition(String name, String description, String thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
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
