package roomescape.exception;

public class ResourceNotExistException extends RuntimeException {

    private static final String message = "[ERROR] 해당 id에 대한 리소스가 존재하지 않습니다.";

    public ResourceNotExistException() {
        super(message);
    }
}
