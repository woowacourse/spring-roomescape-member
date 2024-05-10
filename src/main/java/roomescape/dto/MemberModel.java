package roomescape.dto;

import roomescape.domain.Member;

public record MemberModel(Long id, String name, String email) {
    public static MemberModel from(Member member) {
        return new MemberModel(member.id(), member.name(), member.email());
    }
}
