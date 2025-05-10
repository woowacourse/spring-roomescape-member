package roomescape.dto.member;

import roomescape.model.Member;

public record MemberResponseDto(
        String name
) {
    public static MemberResponseDto from(Member member){
        return new MemberResponseDto(member.getName());
    }
}
