package roomescape.theme.domain;

public record Theme(long id, String name, String description, String thumbnail) {
    private static final long UNDEFINED = 0;

    public Theme(String name, String description, String thumbnail) {
        this(UNDEFINED, name, description, thumbnail);
    }
}
