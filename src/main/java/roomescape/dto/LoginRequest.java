package roomescape.dto;

public record LoginRequest(String password, String email) {

    public LoginRequest {
        InputValidator.validateNotNull(password, email);
        InputValidator.validateNotEmpty(password, email);
    }
}
