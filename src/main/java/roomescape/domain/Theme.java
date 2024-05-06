package roomescape.domain;

public record Theme(long id, String name, String description, String thumbnail) {

    public Theme {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Theme name cannot be null or blank");
        }
    }
}
