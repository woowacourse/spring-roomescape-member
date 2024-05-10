package roomescape.auth.dto;

public record CreateTokenRequest(String email, String password) {

    public CreateTokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
