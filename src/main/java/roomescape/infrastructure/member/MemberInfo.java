package roomescape.infrastructure.member;

import roomescape.entity.member.Role;

public record MemberInfo(Long id, String name, String email, Role role) {

}
