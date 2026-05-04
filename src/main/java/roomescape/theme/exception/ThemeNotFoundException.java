package roomescape.theme.exception;

public class ThemeNotFoundException extends RuntimeException {
    private final long id;

    public ThemeNotFoundException(long id) {
        super("테마가 존재하지 않습니다. id=" + id);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
