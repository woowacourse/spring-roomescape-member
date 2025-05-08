package roomescape.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Member;

public class FakeMemberDaoImpl implements MemberDao {

    private final List<Member> members = new ArrayList<>();
    private final AtomicLong id = new AtomicLong(1);

    @Override
    public Long save(final Member member) {
        member.setId(id.getAndIncrement());
        members.add(member);
        return member.getId();
    }
}
