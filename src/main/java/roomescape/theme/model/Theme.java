package roomescape.theme.model;

import java.time.LocalTime;

public class Theme {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private LocalTime requiredTime;

    public Theme() {
    }

    public Theme(String name, String description, String imageUrl, LocalTime requiredTime) {
        this(null, name, description, imageUrl, requiredTime);
    }

    public Theme(Long id, String name, String description, String imageUrl, LocalTime requiredTime) {
        validateName(name);
        validateRequiredTime(requiredTime);
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.requiredTime = requiredTime;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 필수이며, 공백일 수 없습니다.");
        }
    }

    private void validateRequiredTime(LocalTime requiredTime) {
        if (requiredTime == null) {
            throw new IllegalArgumentException("테마 소요 시간은 필수입니다.");
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

    public LocalTime getRequiredTime() {
        return requiredTime;
    }
}
