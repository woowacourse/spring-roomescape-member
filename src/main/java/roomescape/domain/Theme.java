package roomescape.domain;

import java.util.Objects;

public class Theme {
    private Long id;
    private String name;
    private String description;
    private String url;

    public Theme(Long id, String name, String description, String url) {
        validate(name, description, url);
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
    }

    private void validate(String name, String description, String url) {
        validateName(name);
        Objects.requireNonNull(description, "테마 설명이 비어 있습니다.");
        Objects.requireNonNull(url, "테마 썸네일 주소가 비어 있습니다.");
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마 이름이 비어 있습니다.");
        }
    }

    public Long getId() {
        return id;
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
