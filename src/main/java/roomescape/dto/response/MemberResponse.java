package roomescape.dto.response;

import roomescape.domain.member.Member;

import java.util.List;

public record MemberResponse(String name) {
    public MemberResponse(Member member) {
        this(member.getName());
    }

    public static List<MemberResponse> listOf(List<Member> members) {
        return members.stream()
                .map(MemberResponse::new)
                .toList();
    }
}
