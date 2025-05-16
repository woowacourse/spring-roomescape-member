package roomescape.global.exception.notFound;

public class ThemeNotFoundException extends NotFoundException {
    public ThemeNotFoundException(Long id) {
        super(id, "테마");
    }
}
