package roomescape.theme.domain;

public record Theme(Long id, String name, String description, String thumbnail) {

    private static final Long NOT_SAVED_ID = 0L;

    public static Theme createBeforeSaved(String name, String description, String thumbnail) {
        return new Theme(NOT_SAVED_ID, name, description, thumbnail);
    }

    public Theme {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] id가 null이어서는 안 됩니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름이 null이거나 빈 값이어서는 안 됩니다.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 설명이 null이거나 빈 값이어서는 안 됩니다.");
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 썸네일이 null이거나 빈 값이어서는 안 됩니다.");
        }
    }
}
