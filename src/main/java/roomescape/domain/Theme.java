package roomescape.domain;

import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class Theme {

    private final UUID id;
    private final String name;
    private final String description;
    private final String imageUrl;

    public Theme(UUID id, String name, String description, String imageUrl) {
        validateId(id);
        validateName(name);
        validateDescription(description);
        validateImageUrl(imageUrl);

        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    private void validateId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("테마엔 식별자가 존재해야 합니다.");
        }
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("테마엔 이름이 존재해야 합니다.");
        }
    }

    private void validateDescription(String description) {
        if (!StringUtils.hasText(description)) {
            throw new IllegalArgumentException("테마엔 설명이 존재해야 합니다.");
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) {
            throw new IllegalArgumentException("테마엔 이미지가 존재해야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Theme theme = (Theme) o;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
