package roomescape.domain.theme;

import lombok.Getter;

@Getter
public class Theme {

    private final Long id;
    private final String name;
    private final String content;
    private final String url;

    private Theme(Long id, String name, String content, String url) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.url = url;
    }

    public static Theme of(long id, String name, String content, String url) {
        return new Theme(
            id,
            name,
            content,
            url
        );
    }

    public static Theme createWithoutId(String name, String content, String url) {
        return new Theme(
            null,
            name,
            content,
            url
        );
    }
}
