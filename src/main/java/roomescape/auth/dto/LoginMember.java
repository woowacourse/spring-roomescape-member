package roomescape.auth.dto;

import roomescape.member.MemberRole;

public record LoginMember(Long id, String name, String email, MemberRole role) {
}
