package roomescape.controller.api.dto.response;

import roomescape.controller.api.dto.request.MemberAuthRequest;

public record AuthMemberNameResponse(String name) {

    public static AuthMemberNameResponse from(final MemberAuthRequest request) {
        return new AuthMemberNameResponse(request.name());
    }
}
