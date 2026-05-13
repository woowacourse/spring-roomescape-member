package roomescape.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public record Theme(Long id, String name, String description, String thumbnailUrl) {
    private static final int MAX_NAME_LENGTH = 20;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;

    public Theme {
        validateName(name);
        validateDescription(description);
        validateThumbnailUrl(thumbnailUrl);
    }

    private static void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("테마 이름은 필수입니다.");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 공백일 수 없습니다.");
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("테마 이름은 20자 이하여야 합니다.");
        }
    }

    private static void validateDescription(String description) {
        if (Objects.isNull(description)) {
            throw new IllegalArgumentException("테마 설명은 필수입니다.");
        }

        if (description.isBlank()) {
            throw new IllegalArgumentException("테마 설명은 공백일 수 없습니다.");
        }

        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("테마 설명은 1000자 이하여야 합니다.");
        }
    }

    private static void validateThumbnailUrl(String thumbnailUrl) {
        if (Objects.isNull(thumbnailUrl)) {
            throw new IllegalArgumentException("썸네일 URL은 필수입니다.");
        }

        if (thumbnailUrl.isBlank() || !isUrl(thumbnailUrl)) {
            throw new IllegalArgumentException("썸네일 URL 형식이 올바르지 않습니다.");
        }
    }

    private static boolean isUrl(String thumbnailUrl) {
        try {
            URI uri = new URI(thumbnailUrl);

            return Objects.nonNull(uri.getScheme()) && Objects.nonNull(uri.getHost());
        } catch (URISyntaxException e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Theme theme)) return false;
        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
