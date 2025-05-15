package roomescape.global.auth.dto;

import roomescape.member.domain.MemberRole;

public record UserInfo(Long id, MemberRole memberRole) {
}
