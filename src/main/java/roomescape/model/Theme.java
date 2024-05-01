package roomescape.model;

public class Theme {

    private long themeId;
    private String name;
    private String description;
    private String thumbnail;

    private Theme() {
    }

    public Theme(long themeId, String name, String description, String thumbnail) {
        this.themeId = themeId;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(String name, String description, String thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public long getThemeId() {
        return themeId;
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
