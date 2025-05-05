package roomescape.domain.entity;

public record ReservationTheme(
        Long id,
        String name,
        String description,
        String thumbnail
) {
    public ReservationTheme {
        validateNotBlank(name, description, thumbnail);
    }

    public ReservationTheme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public ReservationTheme assignId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("할당할 id는 null이 될 수 없습니다.");
        }
        return new ReservationTheme(id, name, description, thumbnail);
    }

    private void validateNotBlank(String name, String description, String thumbnail) {
        if (name == null) {
            throw new IllegalArgumentException("테마명은 null이 될 수 없습니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("테마명은 비어 있을 수 없습니다.");
        }
        if (description == null) {
            throw new IllegalArgumentException("테마설명은 null이 될 수 없습니다.");
        }
        if (description.isBlank()) {
            throw new IllegalArgumentException("테마설명은 비어 있을 수 없습니다.");
        }
        if (thumbnail == null) {
            throw new IllegalArgumentException("테마 썸네일은 null이 될 수 없습니다.");
        }
        if (thumbnail.isBlank()) {
            throw new IllegalArgumentException("테마 썸네일은 비어 있을 수 없습니다.");
        }
    }
}
