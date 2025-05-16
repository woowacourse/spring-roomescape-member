package roomescape.domain.member.dto.response;

import roomescape.domain.member.model.Member;

public record MemberResponseDto(String memberName) {

    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(member.getName());
    }
}
