package roomescape.dto;

import java.util.List;
import roomescape.domain.Member;

public record MemberCheckResponse(Long id, String name) {

    public static MemberCheckResponse from(final Member member) {
        return new MemberCheckResponse(member.getId(), member.getName());
    }

    public static List<MemberCheckResponse> fromMembers(final List<Member> members) {
        return members.stream()
                .map(MemberCheckResponse::from)
                .toList();
    }
}
