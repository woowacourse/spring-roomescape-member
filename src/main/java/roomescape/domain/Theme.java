package roomescape.domain;

import java.util.Objects;

public class Theme {

    private static final long DEFAULT_RUNNING_TIME = 60L;

    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final Long runningTime;

    private Theme(Long id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.runningTime = DEFAULT_RUNNING_TIME;
    }

    public static Theme create(String name, String description, String imageUrl) {
        validateName(name);
        validateDescription(description);
        validateImageUrl(imageUrl);
        return new Theme(null, name, description, imageUrl);
    }

    public static Theme of(Long id, String name, String description, String imageUrl) {
        validateId(id);
        validateName(name);
        validateDescription(description);
        validateImageUrl(imageUrl);
        return new Theme(id, name, description, imageUrl);
    }

    private static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID는 필수값입니다.");
        }
        if (id < 1) {
            throw new IllegalArgumentException("ID는 1 이상의 숫자여야 합니다. (입력값: " + id + ")");
        }
    }

    public static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
        if (name.length() > 20) {
            throw new IllegalArgumentException("이름은 20자 이내여야 합니다.");
        }
    }

    public static void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("설명은 필수 입력 사항입니다.");
        }
        if (description.length() < 3) {
            throw new IllegalArgumentException("설명은 최소 5자 이상 작성해주세요.");
        }
    }

    public static void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("이미지 URL은 필수입니다.");
        }
        if (!imageUrl.contains(".")) {
            throw new IllegalArgumentException("이미지 경로 형식이 아닙니다.");
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

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getRunningTime() {
        return runningTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
