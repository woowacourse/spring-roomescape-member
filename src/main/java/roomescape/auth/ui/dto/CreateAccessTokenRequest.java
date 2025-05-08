package roomescape.auth.ui.dto;

public record CreateAccessTokenRequest(
        String email,
        String password
) {

}
