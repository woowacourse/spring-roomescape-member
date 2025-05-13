package roomescape.auth.domain;

public record Token(String value) {

    public Token {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 토큰 값이 빈 값이어서는 안 됩니다.");
        }
    }
}
