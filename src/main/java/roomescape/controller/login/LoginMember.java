package roomescape.controller.login;

import roomescape.domain.Member;

public record LoginMember(Long id, String name, String email, String role) {

    public static LoginMember from(Member member) {
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole().toString());
    }
}
