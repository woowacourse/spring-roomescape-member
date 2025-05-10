package roomescape.auth.controller.dto.response;

import roomescape.auth.service.dto.response.CredentialServiceResponse;

public record CredentialResponse(
        String name
) {

    public static CredentialResponse from(CredentialServiceResponse response) {
        return new CredentialResponse(response.name());
    }
}
