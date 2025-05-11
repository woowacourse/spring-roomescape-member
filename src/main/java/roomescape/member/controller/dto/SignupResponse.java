package roomescape.member.controller.dto;

import roomescape.member.domain.Member;

public record SignupResponse(Long id, String name) {

    public static SignupResponse from(Member member) {
        return new SignupResponse(member.getId(), member.getName());
    }

}
