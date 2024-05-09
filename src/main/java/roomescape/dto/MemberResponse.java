package roomescape.dto;

import java.util.List;
import roomescape.domain.Member;

public record MemberResponse(Long id, String name) {

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getName());
    }

    public static List<MemberResponse> fromMembers(final List<Member> members) {
        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
