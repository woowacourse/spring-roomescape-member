package roomescape.domain.theme.dto;

public class ThemeCreateRequest {

    private final String name;
    private final String description;
    private final String url;

    public ThemeCreateRequest(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
