package roomescape.global.exception.unauthorized;

public class MemberUnauthorizedException extends UnauthorizedException {
    public MemberUnauthorizedException() {
        super("로그인이 필요한 서비스입니다.");
    }
}
