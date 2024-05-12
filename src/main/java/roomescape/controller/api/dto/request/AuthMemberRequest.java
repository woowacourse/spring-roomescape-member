package roomescape.controller.api.dto.request;

import roomescape.service.dto.output.MemberOutput;

public record AuthMemberRequest(Long id, String name, String role) {

    public static AuthMemberRequest from(final MemberOutput output) {
        return new AuthMemberRequest(output.id(), output.name(), output.role());
    }
}
