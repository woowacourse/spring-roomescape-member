package roomescape.member.dto.response;

import roomescape.member.model.Member;

public record FindMemberResponse(Long id, String name) {

    public static FindMemberResponse of(Member member) {
        return new FindMemberResponse(member.getId(), member.getName());
    }
}
