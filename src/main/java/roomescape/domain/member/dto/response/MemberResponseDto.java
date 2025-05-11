package roomescape.domain.member.dto.response;

import roomescape.domain.member.model.Member;

public record MemberResponseDto(String token) {

    // TODO : 추후에 token을 반환하도록 리팩토링 하기
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(member.getName());
    }
}
