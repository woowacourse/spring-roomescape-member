package roomescape.infrastructure.authentication;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("유효한 인가 정보를 입력해주세요.");
    }
}
