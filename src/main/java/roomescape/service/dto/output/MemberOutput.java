package roomescape.service.dto.output;

import roomescape.domain.Member;

public record MemberOutput(long id, String name) {

    public static MemberOutput from(final Member member) {
        return new MemberOutput(member.id(), member.nameAsString());
    }
}
