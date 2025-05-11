package roomescape.global.exception.unauthorized;

public class UnauthorizedException extends IllegalArgumentException {
    public UnauthorizedException(String resource) {
        super(String.format("등록되어 있지 않은 %s 정보입니다.", resource));
    }
}
