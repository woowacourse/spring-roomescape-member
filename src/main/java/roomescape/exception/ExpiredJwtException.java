package roomescape.exception;

public class ExpiredJwtException extends RuntimeException {
    public ExpiredJwtException() {
        super("토큰이 만료되었습니다. 다시 로그인하여 유효한 토큰을 얻어주세요.");
    }
}
