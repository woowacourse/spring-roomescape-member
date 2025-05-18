package roomescape.member.dto;

import roomescape.member.domain.Member;

public record NameResponse(
    String name
) {

    public static NameResponse fromMember(Member member) {
        return new NameResponse(member.getName());
    }
}
