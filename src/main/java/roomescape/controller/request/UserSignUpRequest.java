package roomescape.controller.request;

import roomescape.domain.Member;

public record UserSignUpRequest(String name, String email, String password) {
    public Member toEntity() {
        return new Member(name, email, password);
    }
}
