package roomescape.auth.dto;

import roomescape.member.entity.Member;

public class LoginCheckResponse {

    private final String name;

    private LoginCheckResponse(String name) {
        this.name = name;
    }

    public static LoginCheckResponse from(Member member) {
        return new LoginCheckResponse(member.getName());
    }

    public String getName() {
        return name;
    }
}
