package roomescape.service.dto.request;

import roomescape.controller.dto.request.MemberSignUpRequest;

public record MemberSignUpCreation(String name, String email, String password) {
    public static MemberSignUpCreation from(final MemberSignUpRequest request) {
        return new MemberSignUpCreation(request.name(), request.email(), request.password());
    }
}
