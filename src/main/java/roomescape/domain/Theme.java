package roomescape.domain;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String image;

    private static final int MAX_NAME_LENGTH = 20;

    public Theme(String name, String description, String image) {
        this(null, name, description, image);
    }

    public Theme(Long id, String name, String description, String image) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름은 공백일 수 없습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 이름은 %d자를 초과할 수 없습니다".formatted(MAX_NAME_LENGTH));
        }
    }
}
