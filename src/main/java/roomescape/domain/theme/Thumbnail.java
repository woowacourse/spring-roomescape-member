package roomescape.domain.theme;

public record Thumbnail(String thumbnail) {

    public Thumbnail {
        validateThumbnail(thumbnail);
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail.isEmpty()) {
            throw new IllegalArgumentException("썸네일은 비어있을 수 없습니다.");
        }
    }
}
