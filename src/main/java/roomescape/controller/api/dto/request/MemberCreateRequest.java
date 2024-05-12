package roomescape.controller.api.dto.request;

import roomescape.service.dto.input.MemberCreateInput;

public record MemberCreateRequest(String name, String email, String password, String role) {

    public MemberCreateInput toInput() {
        return new MemberCreateInput(name, email, password, role);
    }
}
