package roomescape.fake;

import roomescape.domain.model.Member;
import roomescape.infrastructure.dao.MemberDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class MemberDaoFake implements MemberDao {

    private static final AtomicLong IDX = new AtomicLong();
    private static final Map<Long, Member> MEMBERS = new HashMap<>(Map.of(
            1L, new Member(1L, "어드민", "admin@gmail.com", "1234", "admin")
    ));

    @Override
    public Long save(final Member member) {
        long id = IDX.getAndIncrement();
        Member savedMember = new Member(id, member.getName(), member.getEmail(), member.getEmail(), member.getRole().getRole());
        MEMBERS.put(id, savedMember);
        return id;
    }

    @Override
    public boolean existByEmail(final String email) {
        long count = MEMBERS.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .count();

        return count != 0;
    }

    @Override
    public Member findByEmail(String email) {
        return MEMBERS.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 멤버가 존재하지 않습니다."));
    }

    @Override
    public Member findById(final Long memberId) {
        return MEMBERS.get(memberId);
    }

    @Override
    public List<Member> findAll() {
        return MEMBERS.values().stream().toList();
    }
}
