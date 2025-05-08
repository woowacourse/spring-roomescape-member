package roomescape.dto.member;

import roomescape.entity.MemberEntity;

public record MemberResponse(String name) {

    public static MemberResponse of(MemberEntity member) {
        return new MemberResponse(member.name());
    }
}
