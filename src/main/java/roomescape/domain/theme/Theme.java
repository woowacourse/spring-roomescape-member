package roomescape.domain.theme;

public class Theme {

    private final Long id;
    private final String name;
    private final String content;
    private final String url;

    public Theme(Long id, String name, String content, String url) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.url = url;
    }
}
