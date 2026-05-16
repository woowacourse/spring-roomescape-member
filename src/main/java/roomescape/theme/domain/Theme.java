package roomescape.theme.domain;

import roomescape.common.exception.DomainValidationException;

public class Theme {
    private Long id;
    private String name;
    private String description;
    private String thumbnailUrl;
    private boolean isActive;

    private Theme(Long id, String name, String description, String thumbnailUrl, boolean isActive) {
        validate(name, description, thumbnailUrl);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.isActive = isActive;
    }

    public static Theme create(String name, String description, String thumbnailUrl) {
        return new Theme(null, name, description, thumbnailUrl, false);
    }

    public static Theme load(Long id, String name, String description, String thumbnailUrl, boolean isActive) {
        return new Theme(id, name, description, thumbnailUrl, isActive);
    }

    private static void validate(String name, String description, String thumbnailUrl) {
        validateName(name);
        validateDescription(description);
        validateThumbnailUrl(thumbnailUrl);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("테마 이름은 필수입니다.");
        }
    }

    private static void validateThumbnailUrl(String thumbnailUrl) {
        if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
            throw new DomainValidationException("테마 썸네일 URL은 필수입니다.");
        }
    }

    private static void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new DomainValidationException("테마 설명은 필수입니다.");
        }
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String thumbnailUrl() {
        return thumbnailUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void updateStatus(boolean isActive) {
        this.isActive = isActive;
    }
}
