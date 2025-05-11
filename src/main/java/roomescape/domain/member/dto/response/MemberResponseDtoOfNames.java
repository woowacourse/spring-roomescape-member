package roomescape.domain.member.dto.response;

import roomescape.domain.member.model.Member;

public record MemberResponseDtoOfNames(Long id, String name) {

    public static MemberResponseDtoOfNames from(Member member) {
        return new MemberResponseDtoOfNames(
            member.getId(),
            member.getName()
        );
    }
}
