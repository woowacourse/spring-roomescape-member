package roomescape.controller.login;

import roomescape.domain.Member;

public record MemberResponse(String email, String password, String name) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getEmail(), member.getPassword(), member.getName());
    }
}
