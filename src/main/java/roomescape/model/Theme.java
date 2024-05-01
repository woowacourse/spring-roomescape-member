package roomescape.model;

public class Theme {

    private long id;
    private String name;
    private String description;
    private String thumbnail;

    private Theme() {
    }

    public Theme(long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public long getId() {
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
