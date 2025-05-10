package roomescape.auth.web.controller.dto;

public record LoginRequest(
        String email,
        String password
) {

}
