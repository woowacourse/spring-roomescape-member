package roomescape.service.dto.request;

public record LoginServiceRequest(
        String email,
        String password
) {
}
