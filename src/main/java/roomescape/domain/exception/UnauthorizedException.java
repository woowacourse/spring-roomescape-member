package roomescape.domain.exception;

public class UnauthorizedException extends RuntimeException {

    private static final String message = "[ERROR] 회원 정보가 올바르지 않습니다.";

    public UnauthorizedException() {
        super(message);
    }
}
