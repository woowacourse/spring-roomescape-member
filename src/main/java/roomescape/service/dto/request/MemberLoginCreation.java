package roomescape.service.dto.request;

import roomescape.controller.dto.request.MemberLoginRequest;

public record MemberLoginCreation(String email, String password) {

    public static MemberLoginCreation from(final MemberLoginRequest request) {
        return new MemberLoginCreation(request.email(), request.password());
    }
}
