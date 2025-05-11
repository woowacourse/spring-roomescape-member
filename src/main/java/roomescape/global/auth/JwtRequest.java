package roomescape.global.auth;

import roomescape.domain.member.model.Member;
import roomescape.domain.member.model.Role;

public record JwtRequest(Long id, String name, Role role) {

    public static JwtRequest from(Member member) {
        return new JwtRequest(member.getId(), member.getName(), member.getRole());
    }
}
