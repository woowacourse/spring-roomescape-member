package roomescape.member.service;

import roomescape.member.auth.dto.MemberInfo;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberEmail;
import roomescape.member.domain.MemberId;
import roomescape.member.domain.MemberName;

public class MemberConverter {

    public static MemberInfo toDto(Member member) {
        return new MemberInfo(
                member.getId().getValue(),
                member.getName().getValue(),
                member.getEmail().getValue(),
                member.getRole());
    }

    public static Member toDomain(MemberInfo memberInfo) {
        return Member.withId(
                MemberId.from(memberInfo.id()),
                MemberName.from(memberInfo.name()),
                MemberEmail.from(memberInfo.email()),
                memberInfo.role());
    }
}
