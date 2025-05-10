package roomescape.auth.domain;

import roomescape.member.domain.MemberRole;

public record LoginMember(Long id, String name, MemberRole memberRole) {
}
