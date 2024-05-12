package roomescape.controller.api.dto.response;

import roomescape.controller.api.dto.request.AuthMemberRequest;

public record AuthMemberNameResponse(String name) {

    public static AuthMemberNameResponse from(final AuthMemberRequest request) {
        return new AuthMemberNameResponse(request.name());
    }
}
