package roomescape.domain.reservation.theme;

import java.util.Objects;

public class Theme {
    private Long id;
    private ThemeName name;
    private String description;
    private String url;

    public Theme(Long id, ThemeName name, String description, String url) {
        validate(name, description, url);
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
    }

    private void validate(ThemeName name, String description, String url) {
        Objects.requireNonNull(name, "테마 이름이 비어 있습니다.");
        Objects.requireNonNull(description, "테마 설명이 비어 있습니다.");
        Objects.requireNonNull(url, "테마 썸네일 주소가 비어 있습니다.");
    }

    public Long getId() {
        return id;
    }

    public ThemeName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
