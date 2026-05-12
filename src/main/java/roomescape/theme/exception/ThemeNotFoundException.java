package roomescape.theme.exception;

public class ThemeNotFoundException extends RuntimeException {
    private final Long id;

    public ThemeNotFoundException(Long id) {
        super("테마가 존재하지 않습니다. id=" + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
