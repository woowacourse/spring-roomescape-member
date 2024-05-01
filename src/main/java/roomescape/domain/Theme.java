package roomescape.domain;

public class Theme {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;

    public Theme(final Long id, final String name, final String description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }
}
