package roomescape.exception;

public class NotFoundException extends RuntimeException{ //TODO 나머지 Dao도 예외처리

    public NotFoundException(String message) {
        super(message);
    }
}
