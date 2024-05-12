package roomescape.controller.api.dto.request;

import roomescape.service.dto.input.MemberLoginInput;

public record MemberLoginRequest(String password, String email) {

    public MemberLoginInput toInput() {
        return new MemberLoginInput(password, email);
    }
}
