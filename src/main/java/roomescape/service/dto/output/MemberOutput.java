package roomescape.service.dto.output;

import java.util.List;
import roomescape.domain.Member;

public record MemberOutput(long id, String name, String email, String password, String role) {

    public static MemberOutput from(final Member member) {
        return new MemberOutput(
                member.id(),
                member.nameAsString(),
                member.emailAsString(),
                member.passwordAsString(),
                member.roleAsString()
        );
    }

    public static List<MemberOutput> list(final List<Member> members) {
        return members.stream()
                .map(MemberOutput::from)
                .toList();
    }
}
