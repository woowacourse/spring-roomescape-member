package roomescape.fake;

import roomescape.domain.model.Member;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemberDao {

    private static final AtomicLong IDX = new AtomicLong();
    private static final Map<Long, Member> MEMBERS = Map.of(
            1L, new Member(1L, "메이", "admin@gmail.com", "1234")
    );

    public Member findByEmailAndPassword(String email, String password) {
        return MEMBERS.values().stream()
                .filter(member -> member.getEmail().equals(email) && member.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 멤버가 존재하지 않습니다."));
    }

    public Member findById(final Long memberId) {
        return MEMBERS.get(memberId);
    }

    public List<Member> findAll() {
        return MEMBERS.values().stream().toList();
    }
}
