package roomescape.global.exception.notFound;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException(Long id) {
        super(id, "유저");
    }
}
