package roomescape.auth.controller.dto;

public class TokenRequest {
    private String email;
    private String password;

    public TokenRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
