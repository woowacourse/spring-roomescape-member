package roomescape.infrastructure.auth;

public class Token {
    private final String token;

    public Token(String token) {
        validateToken(token);
        this.token = token;
    }

    private void validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("부적절한 토큰 값입니다.");
        }
    }

    public String getToken() {
        return token;
    }
}
