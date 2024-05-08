package roomescape.domain.theme;

import java.util.Objects;

public record Thumbnail(String thumbnail) {

    public Thumbnail {
        validateNull(thumbnail);
        validateEmptiness(thumbnail);
    }

    private void validateNull(String thumbnail) {
        if (Objects.isNull(thumbnail)) {
            throw new IllegalArgumentException("썸네일 값은 null이 될 수 없습니다.");
        }
    }

    private void validateEmptiness(String thumbnail) {
        if (thumbnail.isEmpty()) {
            throw new IllegalArgumentException("썸네일은 비어있을 수 없습니다.");
        }
    }
}
