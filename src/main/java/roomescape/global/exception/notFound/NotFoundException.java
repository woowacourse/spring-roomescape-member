package roomescape.global.exception.notFound;

public class NotFoundException extends IllegalArgumentException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Long id, String resource) {
        super(String.format("%d 식별자를 갖는 %s이(가) 존재하지 않습니다.", id, resource));
    }
}
