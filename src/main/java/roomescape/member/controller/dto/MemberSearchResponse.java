package roomescape.member.controller.dto;

import roomescape.member.domain.Member;

public record MemberSearchResponse(Long id, String name) {

    public static MemberSearchResponse from(Member member) {
        return new MemberSearchResponse(member.getId(), member.getName());
    }

}
