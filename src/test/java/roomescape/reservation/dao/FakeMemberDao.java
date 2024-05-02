package roomescape.reservation.dao;

import java.util.HashMap;
import java.util.Map;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;

public class FakeMemberDao implements MemberRepository {

    private final Map<Long, Member> members = new HashMap<>();

    @Override
    public Member save(final Member member) {
        members.put((long) members.size() + 1, member);
        return new Member(
                (long) members.size(),
                member.getName()
        );
    }
}
