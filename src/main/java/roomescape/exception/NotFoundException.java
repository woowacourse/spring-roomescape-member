package roomescape.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String name) {
        super(name + "을 찾을 수 없습니다.");
    }
}
