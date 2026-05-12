package roomescape.domain;

import java.util.Objects;
import org.springframework.util.StringUtils;

public record Theme(
        EntityId id,
        String name,
        String description,
        String imageUrl
) {

    public Theme {
        validateId(id);
        validateName(name);
        validateDescription(description);
        validateImageUrl(imageUrl);
    }

    private void validateId(EntityId id) {
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
