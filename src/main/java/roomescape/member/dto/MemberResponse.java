package roomescape.member.dto;

import java.util.List;
import roomescape.member.domain.Member;

public record MemberResponse(Long id, String name, String email) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public static List<MemberResponse> from(List<Member> members) {
        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
