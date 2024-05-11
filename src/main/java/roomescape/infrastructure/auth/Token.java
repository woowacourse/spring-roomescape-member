package roomescape.infrastructure.auth;

// TODO null check, empty check등 조금 더 의미부여하기
public class Token {
    private final String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
