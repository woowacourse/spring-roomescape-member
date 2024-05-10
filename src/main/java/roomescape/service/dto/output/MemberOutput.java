package roomescape.service.dto.output;

import java.util.List;
import roomescape.domain.Member;

public record MemberOutput(long id, String name) {

    public static MemberOutput from(final Member member) {
        return new MemberOutput(member.id(), member.nameAsString());
    }

    public static List<MemberOutput> list(final List<Member> members) {
        return members.stream()
                .map(MemberOutput::from)
                .toList();
    }
}
