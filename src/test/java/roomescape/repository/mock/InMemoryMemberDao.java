package roomescape.repository.mock;

import java.util.List;
import roomescape.domain.Member;
import roomescape.repository.MemberDao;

public class InMemoryMemberDao implements MemberDao {

    public List<Member> members;

    @Override
    public List<Member> findAll() {
        return members;
    }

    @Override
    public Member findByEmail(final String email) {
        return members.stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 이메일입니다."));
    }

    @Override
    public Member findById(final Long id) {
        return members.stream()
                .filter(member -> member.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 아이디입니다."));
    }
}
