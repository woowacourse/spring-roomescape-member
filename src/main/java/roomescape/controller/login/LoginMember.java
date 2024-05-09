package roomescape.controller.login;

import roomescape.domain.Member;

public record LoginMember(String email, String password, String name) {
    
    public static LoginMember from(final Member member) {
        return new LoginMember(member.getEmail(), member.getPassword(), member.getName());
    }
}
