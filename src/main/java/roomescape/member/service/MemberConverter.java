package roomescape.member.service;

import roomescape.member.auth.dto.MemberInfo;
import roomescape.member.domain.Member;

public class MemberConverter {

    public static MemberInfo toResponse(Member member) {
        return new MemberInfo(
                member.getId().getValue(),
                member.getName().getValue(),
                member.getEmail().getValue());
    }

    public static MemberInfo toDto(Member member) {
        return new MemberInfo(
                member.getId().getValue(),
                member.getName().getValue(),
                member.getEmail().getValue());
    }
}
