package roomescape.exception.notFound;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super(id, "유저");
    }
}
