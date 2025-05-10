package roomescape.auth.service.dto.response;

import roomescape.auth.infrastructure.dto.CredentialDetails;

public record CredentialServiceResponse(
        String name
) {

    public static CredentialServiceResponse from(CredentialDetails credentialDetails) {
        return new CredentialServiceResponse(credentialDetails.name());
    }
}
